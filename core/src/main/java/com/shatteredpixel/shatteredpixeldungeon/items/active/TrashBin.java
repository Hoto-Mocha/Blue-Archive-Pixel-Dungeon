package com.shatteredpixel.shatteredpixeldungeon.items.active;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TrashBin extends Item {

	{
		image = ItemSpriteSheet.TRASH_BIN;
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
			if (Dungeon.hero.buff(TrashBinCooldown.class) == null) {
				for (Char ch : Actor.chars()) {
					if (ch instanceof Mob) {
						new FlavourBuff(){
							{actPriority = VFX_PRIO;}
							public boolean act() {
								if (((Mob) ch).state == ((Mob) ch).HUNTING){
									((Mob) ch).state = ((Mob) ch).WANDERING;
									((Mob) ch).beckon(Dungeon.level.randomDestination(ch));
									ch.sprite.showLost();
								}
								return super.act();
							}
						}.attachTo(ch);
					}
				}
				Dungeon.hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_bin_use_" + (1+Random.Int(2))));
				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);
				CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
				curUser.spendAndNext(Actor.TICK);

				Buff.affect(hero, TrashBinCooldown.class).set();
			} else {
				Dungeon.hero.yellN(Messages.get(Hero.class, hero.heroClass.name() + "_bin_cooldown"));	//"...아직은 사용할 수 없어요..."
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

	public static class TrashBinCooldown extends Buff {

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
			icon.hardlight(0x2091DB);
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
