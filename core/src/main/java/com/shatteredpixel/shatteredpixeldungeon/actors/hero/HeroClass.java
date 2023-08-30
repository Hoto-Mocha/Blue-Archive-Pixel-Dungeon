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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.Aris_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.Aris_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.Aris_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.Hoshino_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.Hoshino_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.Hoshino_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.Miyako_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.Miyako_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.Miyako_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.Nonomi_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.Nonomi_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.Nonomi_3;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BlastGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Claymore;
import com.shatteredpixel.shatteredpixeldungeon.items.active.HandGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.IronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.SmokeGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.MG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.MG_tier5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.SG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.SMG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	ARIS( HeroSubClass.ARIS_EX_SUPERNOVA, HeroSubClass.ARIS_EX_CHARGE ),
	NONOMI( HeroSubClass.NONOMI_EX_RIOT, HeroSubClass.NONOMI_EX_SHOOTALL ),
	MIYAKO( HeroSubClass.MIYAKO_EX_STUNDRONE, HeroSubClass.MIYAKO_EX_DRONESTRIKE ),
	HOSHINO( HeroSubClass.HOSHINO_EX_TACTICAL_PRESS, HeroSubClass.HOSHINO_EX_TACTICAL_SHIELD ),

	DUELIST( HeroSubClass.CHAMPION, HeroSubClass.MONK );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new PotionOfExperience().identify().quantity(30).collect();
		new TengusMask().collect();
		new ScrollOfUpgrade().identify().quantity(200).collect();
		new MG_tier5().collect();
		new ScrollOfIdentify().collect();
		new ScrollOfTransmutation().identify().quantity(200).collect();
		new ScrollOfLullaby().identify().quantity(200).collect();

		new ScrollOfIdentify().identify();

		switch (this) {
			case ARIS:
				initAris( hero );
				break;

			case NONOMI:
				initNonomi( hero );
				break;

			case MIYAKO:
				initMiyako( hero );
				break;

			case HOSHINO:
				initHoshino( hero );
				break;

			case DUELIST:
				initDuelist( hero );
				break;
		}

		if (SPDSettings.quickslotWaterskin()) {
			for (int s = 0; s < QuickSlot.SIZE; s++) {
				if (Dungeon.quickslot.getItem(s) == null) {
					Dungeon.quickslot.setSlot(s, waterskin);
					break;
				}
			}
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case ARIS:
				return Badges.Badge.MASTERY_ARIS;
			case NONOMI:
				return Badges.Badge.MASTERY_NONOMI;
			case MIYAKO:
				return Badges.Badge.MASTERY_MIYAKO;
			case HOSHINO:
				return Badges.Badge.MASTERY_HOSHINO;
			case DUELIST:
				return Badges.Badge.MASTERY_DUELIST;
		}
		return null;
	}

	private static void initAris( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(1, stones);
		SuperNova superNova = new SuperNova();
		superNova.collect();
		Dungeon.quickslot.setSlot(0, superNova);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initNonomi( Hero hero ) {
		MG_tier1 mg = new MG_tier1();

		(hero.belongings.weapon = mg).identify();
		hero.belongings.weapon.activate(hero);

		SmokeGrenade smokeGrenade = new SmokeGrenade();
		smokeGrenade.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, mg);
		Dungeon.quickslot.setSlot(1, smokeGrenade);
		Dungeon.quickslot.setSlot(2, knives);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initMiyako( Hero hero ) {
		SMG_tier1 smg = new SMG_tier1();

		(hero.belongings.weapon = smg).identify();
		hero.belongings.weapon.activate(hero);

		HandGrenade handGrenade = new HandGrenade();
		handGrenade.collect();

		Claymore claymore = new Claymore();
		claymore.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, smg);
		Dungeon.quickslot.setSlot(1, handGrenade);
		Dungeon.quickslot.setSlot(2, claymore);
		Dungeon.quickslot.setSlot(3, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHoshino( Hero hero ) {

		SG_tier1 sg = new SG_tier1();

		(hero.belongings.weapon = sg).identify();
		hero.belongings.weapon.activate(hero);

		IronHorus shield = new IronHorus();
		shield.collect();

		BlastGrenade blastGrenade = new BlastGrenade();
		blastGrenade.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, sg);
		Dungeon.quickslot.setSlot(1, shield);
		Dungeon.quickslot.setSlot(2, blastGrenade);
		Dungeon.quickslot.setSlot(3, knives);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	private static void initDuelist( Hero hero ) {

		(hero.belongings.weapon = new Rapier()).identify();
		hero.belongings.weapon.activate(hero);

		ThrowingSpike spikes = new ThrowingSpike();
		spikes.quantity(2).collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, spikes);

		new PotionOfStrength().identify();
		new ScrollOfMirrorImage().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case ARIS: default:
				return new ArmorAbility[]{new Aris_1(), new Aris_2(), new Aris_3()};
			case NONOMI:
				return new ArmorAbility[]{new Nonomi_1(), new Nonomi_2(), new Nonomi_3()};
			case MIYAKO:
				return new ArmorAbility[]{new Miyako_1(), new Miyako_2(), new Miyako_3()};
			case HOSHINO:
				return new ArmorAbility[]{new Hoshino_1(), new Hoshino_2(), new Hoshino_3()};
			case DUELIST:
				return new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
		}
	}

	public String spritesheet() {
		switch (this) {
			case ARIS: default:
				return Assets.Sprites.ARIS;
			case NONOMI:
				return Assets.Sprites.NONOMI;
			case MIYAKO:
				return Assets.Sprites.MIYAKO;
			case HOSHINO:
				return Assets.Sprites.HOSHINO;
			case DUELIST:
				return Assets.Sprites.DUELIST;
		}
	}

	public String splashArt(){
		switch (this) {
			case ARIS: default:
				return Assets.Splashes.ARIS;
			case NONOMI:
				return Assets.Splashes.NONOMI;
			case MIYAKO:
				return Assets.Splashes.MIYAKO;
			case HOSHINO:
				return Assets.Splashes.HOSHINO;
			case DUELIST:
				return Assets.Splashes.DUELIST;
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;

		switch (this){
			case ARIS: default:
				return true;
			case NONOMI:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_NONOMI);
			case MIYAKO:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MIYAKO);
			case HOSHINO:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HOSHINO);
			case DUELIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
