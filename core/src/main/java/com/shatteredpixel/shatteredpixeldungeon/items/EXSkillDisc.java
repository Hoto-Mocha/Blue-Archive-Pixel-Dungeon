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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DroneStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StunDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChooseSubclass;

import java.util.ArrayList;

public class EXSkillDisc extends TengusMask {
	
	private static final String AC_USE	= "USE";
	
	{
		stackable = false;
		image = ItemSpriteSheet.EX_BD;

		defaultAction = AC_USE;

		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_USE );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_USE )) {
			
			curUser = hero;

			GameScene.show( new WndChooseSubclass( this, hero ) );
			
		}
	}
	
	@Override
	public boolean doPickUp(Hero hero, int pos) {
		Badges.validateMastery();
		return super.doPickUp( hero, pos );
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
	public void choose( HeroSubClass way ) {
		
		super.choose(way);

		if (curUser.subClass == HeroSubClass.MIYAKO_EX_STUNDRONE && curUser.buff(StunDrone.class) == null) {
			Buff.affect(hero, StunDrone.class);
		}

		if (curUser.subClass == HeroSubClass.MIYAKO_EX_DRONESTRIKE && curUser.buff(DroneStrike.class) == null) {
			Buff.affect(hero, DroneStrike.class);
		}
		
	}
}
