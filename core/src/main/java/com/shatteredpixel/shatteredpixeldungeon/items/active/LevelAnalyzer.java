package com.shatteredpixel.shatteredpixeldungeon.items.active;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LevelAnalyzer extends Item {

	{
		image = ItemSpriteSheet.ANALYZER;
		levelKnown = true;

		defaultAction = AC_USE;
		usesTargeting = false;

		bones = false;
		unique = true;
	}

	private static final String AC_USE = "USE";

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_USE );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute( hero, action );
		if (action.equals(AC_USE)) {
			int dist = 3+buffedLvl();
			if (Dungeon.hero.buff(LevelAnalyzerCooldown.class) == null) {

				Point c = Dungeon.level.cellToPoint(curUser.pos);

				int[] rounding = ShadowCaster.rounding[dist];

				int left, right;
				int curr;
				boolean noticed = false;
				for (int y = Math.max(0, c.y - dist); y <= Math.min(Dungeon.level.height()-1, c.y + dist); y++) {
					if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
						left = c.x - rounding[Math.abs(c.y - y)];
					} else {
						left = dist;
						while (rounding[left] < rounding[Math.abs(c.y - y)]){
							left--;
						}
						left = c.x - left;
					}
					right = Math.min(Dungeon.level.width()-1, c.x + c.x - left);
					left = Math.max(0, left);
					for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++){

						GameScene.effectOverFog( new CheckedCell( curr, curUser.pos ) );
						Dungeon.level.mapped[curr] = true;

						if (Dungeon.level.secret[curr]) {
							Dungeon.level.discover(curr);

							if (Dungeon.level.heroFOV[curr]) {
								GameScene.discoverTile(curr, Dungeon.level.map[curr]);
								ScrollOfMagicMapping.discover(curr);
								noticed = true;
							}
						}

					}
				}

				if (noticed) {
					Sample.INSTANCE.play( Assets.Sounds.SECRET );
				}

				Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
				GameScene.updateFog();

				Dungeon.hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_analyzer_use"));	//"주변을 스캔합니다..."

				curUser.sprite.operate(curUser.pos);
				curUser.spendAndNext(Actor.TICK);
				Buff.affect(curUser, LevelAnalyzerCooldown.class).set();
			} else {
				Dungeon.hero.yellN(Messages.get(Hero.class, hero.heroClass.name() + "_analyzer_cooldown"));	//"...아직은 사용할 수 없어요..."
			}
		}
	}

	@Override
	public int level() {
		int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
		return level;
	}

	@Override
	public int buffedLvl() {
		//level isn't affected by buffs/debuffs
		return level();
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int value() {
		return -1;
	}

	public static class LevelAnalyzerCooldown extends Buff {

		{
			type = buffType.NEUTRAL;
			announced = false;
		}

		private int coolDown;
		private int maxCoolDown;

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xE2A865);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (maxCoolDown - coolDown)/ maxCoolDown);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(coolDown);
		}

		public void kill() {
			coolDown --;
			if (coolDown <= 0) {
				detach();
			}
			BuffIndicator.refreshHero();    //영웅의 버프창 갱신
		}

		public void set() {
			maxCoolDown = 5;
			coolDown = maxCoolDown;
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", coolDown, maxCoolDown);
		}

		private static final String MAXCOOLDOWN = "maxCoolDown";
		private static final String COOLDOWN  = "cooldown";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(MAXCOOLDOWN, maxCoolDown);
			bundle.put(COOLDOWN, coolDown);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			maxCoolDown = bundle.getInt( MAXCOOLDOWN );
			coolDown = bundle.getInt( COOLDOWN );
		}
	}
}
