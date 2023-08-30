package com.shatteredpixel.shatteredpixeldungeon.items.active;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class IronHorus extends Item {

	{
		image = ItemSpriteSheet.IRON_HORUS;

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
			if (Dungeon.hero.buff(TacticalShieldCooldown.class) == null) {
				Buff.affect(curUser, TacticalShieldBuff.class);
				curUser.sprite.operate(curUser.pos);
				curUser.spendAndNext(Actor.TICK);
			} else {
				Dungeon.hero.yellN(Messages.get(Hero.class, "shield_cooldown"));
			}
		}
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
	public int value() {
		return -1;
	}

	public static class TacticalShieldBuff extends Buff {

		{
			type = buffType.NEUTRAL;
			announced = false;
		}

		public int pos = -1;

		@Override
		public boolean act() {
			if (pos != target.pos) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}

		public boolean attachTo(Char target ) {
			if (super.attachTo(target)) {
				pos = target.pos;
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR_LIGHT;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xFF9AB0);
		}

		@Override
		public void detach() {
			super.detach();
			float time = 10;
			Buff invisibilityTracker = Dungeon.hero.buff(Talent.TacticalInvisibilityTracker.class);
			if (Dungeon.hero.hasTalent(Talent.QUICK_RETREAT)) {
				time -= 2*Dungeon.hero.pointsInTalent(Talent.QUICK_RETREAT);
			}
			if (invisibilityTracker != null) {
				time *= 3;
				invisibilityTracker.detach();
			}
			Buff.affect(target, TacticalShieldCooldown.class, time);
		}

		public int armorBonus(){
			if (pos == target.pos && target instanceof Hero){
				int level = Dungeon.hero.lvl/5;
				return Random.NormalIntRange(0, (4+Dungeon.hero.pointsInTalent(Talent.HARD_SHIELD))*(level+1));
			} else {
				detach();
				return 0;
			}
		}

		@Override
		public String desc() {
			int level = Dungeon.hero.lvl/5;
			return Messages.get(this, "desc", (4+Dungeon.hero.pointsInTalent(Talent.HARD_SHIELD))*(level+1));
		}

		private static final String POS = "pos";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
	}

	public static class TacticalShieldCooldown extends FlavourBuff {

		{
			type = buffType.NEUTRAL;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xFF9AB0);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (10f - visualcooldown()) / 10f);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}
	}
}
