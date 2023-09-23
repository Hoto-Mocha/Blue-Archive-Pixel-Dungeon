/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class InfiniteBullet extends Buff {

	float maxDuration = 3;
	float duration = 0;
	int maxAddition = 0;

	{
		type = buffType.POSITIVE;
		announced = true;
	}

	private static final String MAX_DURATION = "maxDuration";
	private static final String DURATION = "duration";
	private static final String MAX_ADDITION = "maxAddition";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(MAX_DURATION, maxDuration);
		bundle.put(DURATION, duration);
		bundle.put(MAX_ADDITION, maxAddition);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		maxDuration = bundle.getFloat(MAX_DURATION);
		duration = bundle.getFloat(DURATION);
		maxAddition = bundle.getInt(MAX_ADDITION);
	}

	public void set(float amount) {
		duration = maxDuration + amount;
	}

	public void add() {
		if (maxAddition < 2*Dungeon.hero.pointsInTalent(Talent.LARGE_MAGAZINE_2)) {
			maxAddition ++;
			duration ++;
			duration = Math.min(maxDuration, duration);
		}
	}

	@Override
	public boolean act() {
		duration -= TICK;
		spend(TICK);
		if (duration <= 0) {
			detach();
		}

		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.INVERT_MARK;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0x3D0067);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (maxDuration - duration) / maxDuration);
	}

	public String desc(){
		return Messages.get(this, "desc", dispTurns(duration));
	}

	public static class infiniteBulletCooldown extends Buff {

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
			icon.hardlight(0x3D0067);
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
