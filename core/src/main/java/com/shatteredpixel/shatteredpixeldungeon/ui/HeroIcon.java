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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

//icons for hero subclasses and abilities atm, maybe add classes?
public class HeroIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	//transparent icon
	public static final int NONE    = 63;

	//subclasses
	public static final int BERSERKER   = 0;
	public static final int GLADIATOR   = 1;
	public static final int BATTLEMAGE  = 2;
	public static final int WARLOCK     = 3;
	public static final int ASSASSIN    = 4;
	public static final int FREERUNNER  = 5;
	public static final int SNIPER      = 6;
	public static final int WARDEN      = 7;
	public static final int CHAMPION    = 8;
	public static final int MONK        = 9;

	//abilities
	public static final int HEROIC_LEAP     = 16;
	public static final int SHOCKWAVE       = 17;
	public static final int ENDURE          = 18;
	public static final int ELEMENTAL_BLAST = 19;
	public static final int WILD_MAGIC      = 20;
	public static final int WARP_BEACON     = 21;
	public static final int SMOKE_BOMB      = 22;
	public static final int DEATH_MARK      = 23;
	public static final int SHADOW_CLONE    = 24;
	public static final int SPECTRAL_BLADES = 25;
	public static final int NATURES_POWER   = 26;
	public static final int SPIRIT_HAWK     = 27;
	public static final int CHALLENGE       = 28;
	public static final int ELEMENTAL_STRIKE= 29;
	public static final int FEINT           = 30;
	public static final int RATMOGRIFY      = 31;

	//action indicator visuals
	public static final int BERSERK         = 32;
	public static final int COMBO           = 33;
	public static final int PREPARATION     = 34;
	public static final int MOMENTUM        = 35;
	public static final int SNIPERS_MARK    = 36;
	public static final int WEAPON_SWAP     = 37;
	public static final int MONK_ABILITIES  = 38;

	//new subclasses
	public static final int ARIS_EX_SUPERNOVA  				= 40;
	public static final int ARIS_EX_CHARGE					= 41;
	public static final int NONOMI_EX_RIOT					= 42;
	public static final int NONOMI_EX_SHOOTALL				= 43;
	public static final int MIYAKO_EX_STUNDRONE				= 44;
	public static final int MIYAKO_EX_DRONESTRIKE			= 45;
	public static final int HOSHINO_EX_TACTICAL_PRESS		= 46;
	public static final int HOSHINO_EX_TACTICAL_SHIELD		= 47;
	public static final int SHIROKO_EX_ELEMENTAL_BULLET		= 48;
	public static final int SHIROKO_EX_PROFESSIONAL_RIDE	= 49;
	public static final int NOA_EX_LARGE_MAGAZINE			= 50;
	public static final int NOA_EX_DOUBLE_BARREL			= 51;
//	public static final int 								= 52;
//	public static final int 								= 53;
//	public static final int 								= 54;
//	public static final int 								= 55;
//	public static final int 								= 56;
//	public static final int 								= 57;
//	public static final int 								= 58;
//	public static final int 								= 59;
//	public static final int 								= 60;
//	public static final int 								= 61;
//	public static final int 								= 62;
//	public static final int 								= 63;

	public static final int ARIS_1			= 64;
	public static final int ARIS_2			= 65;
	public static final int ARIS_3			= 66;
	public static final int NONOMI_1		= 67;
	public static final int NONOMI_2		= 68;
	public static final int NONOMI_3		= 69;
	public static final int MIYAKO_1		= 70;
	public static final int MIYAKO_2		= 71;
	public static final int MIYAKO_3		= 72;
	public static final int HOSHINO_1		= 73;
	public static final int HOSHINO_2		= 74;
	public static final int HOSHINO_3	 	= 75;
	public static final int SHIROKO_1		= 76;
	public static final int SHIROKO_2		= 77;
	public static final int SHIROKO_3		= 78;
	public static final int NOA_1			= 79;
	public static final int NOA_2			= 80;
	public static final int NOA_3			= 81;
//	public static final int 				= 82;
//	public static final int 				= 83;
//	public static final int 				= 84;
//	public static final int 				= 85;
//	public static final int 				= 86;
//	public static final int 				= 87;
//	public static final int 				= 88;
//	public static final int 				= 89;
//	public static final int 				= 90;
//	public static final int 				= 91;
//	public static final int 				= 92;
//	public static final int 				= 93;
//	public static final int 				= 94;
//	public static final int 				= 95;
//	public static final int 				= 96;

	//new abilities
	public static final int STUNDRONE			= 97;
	public static final int DRONESTRIKE			= 98;
	public static final int ELEMENTAL_BULLET	= 99;
	public static final int PROFESSIONAL_RIDE	= 100;
	public static final int DOUBLE_BARREL		= 101;





	public HeroIcon(HeroSubClass subCls){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(subCls.icon()));
	}

	public HeroIcon(ArmorAbility abil){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(abil.icon()));
	}

	public HeroIcon(ActionIndicator.Action action){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(action.actionIcon()));
	}

}
