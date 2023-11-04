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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.Miyu_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.Miyu_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.Miyu_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noa.Noa_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noa.Noa_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noa.Noa_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.Nonomi_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.Nonomi_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.Nonomi_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.Shiroko_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.Shiroko_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.Shiroko_3;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.Yuzu_1;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.Yuzu_2;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.Yuzu_3;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EXSkillDisc;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Bicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BlastGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Claymore;
import com.shatteredpixel.shatteredpixeldungeon.items.active.FlashGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.HPGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.HandGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.IronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Laser;
import com.shatteredpixel.shatteredpixeldungeon.items.active.LevelAnalyzer;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RainbowGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RegrowGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.SmokeGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.items.active.TrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.AR_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.GL_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.GL_tier2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.HG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.HG_tier2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.HG_tier3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.HG_tier4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.HG_tier5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.MG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.MG_tier2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.MG_tier5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.SG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.SG_tier2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.SMG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.SMG_tier2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.SR_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	ARIS( HeroSubClass.ARIS_EX_SUPERNOVA, HeroSubClass.ARIS_EX_CHARGE ),
	NONOMI( HeroSubClass.NONOMI_EX_RIOT, HeroSubClass.NONOMI_EX_SHOOTALL ),
	MIYAKO( HeroSubClass.MIYAKO_EX_STUNDRONE, HeroSubClass.MIYAKO_EX_DRONESTRIKE ),
	HOSHINO( HeroSubClass.HOSHINO_EX_TACTICAL_PRESS, HeroSubClass.HOSHINO_EX_TACTICAL_SHIELD ),
	SHIROKO( HeroSubClass.SHIROKO_EX_ELEMENTAL_BULLET, HeroSubClass.SHIROKO_EX_PROFESSIONAL_RIDE ),
	NOA(HeroSubClass.NOA_EX_LARGE_MAGAZINE, HeroSubClass.NOA_EX_DOUBLE_BARREL),
	MIYU(HeroSubClass.MIYU_EX_PENETRATION_SHOT, HeroSubClass.MIYU_EX_SNIPING_BULLET),
	YUZU(HeroSubClass.YUZU_EX_GAME_START, HeroSubClass.YUZU_EX_STICKY_GRENADE);

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

		if (SPDSettings.customSeed().equals("test")) {
			new PotionOfExperience().identify().quantity(30).collect();
			new RingOfMight().identify().upgrade(10).collect();
			new EXSkillDisc().collect();
			new SMG_tier2().collect();
			new SG_tier2().identify().collect();
			new CurseInfusion().quantity(200).collect();
			new ScrollOfUpgrade().identify().quantity(200).collect();
//			new StoneOfAugmentation().quantity(200).collect();
//			new PotionOfMindVision().identify().quantity(200).collect();
//			new RingOfWealth().identify().upgrade(100).collect();
			new PlateArmor().identify().upgrade(100).collect();
			new RingOfFuror().identify().upgrade(100).collect();
			new RingOfHaste().identify().upgrade(100).collect();
			new Teleporter().collect();
			new KingsCrown().collect();
			new Pasty().quantity(200).collect();
			new PotionOfMindVision().identify().quantity(200).collect();
//			new ScrollOfEnchantment().identify().quantity(200).collect();
			new Amulet().collect();
//			new ScrollOfTransmutation().identify().collect();
		}

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

			case SHIROKO:
				initShiroko( hero );
				break;

			case NOA:
				initNoa( hero );
				break;

			case MIYU:
				initMiyu( hero );
				break;

			case YUZU:
				initYuzu( hero );
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
			case SHIROKO:
				return Badges.Badge.MASTERY_SHIROKO;
			case NOA:
				return Badges.Badge.MASTERY_NOA;
			case MIYU:
				return Badges.Badge.MASTERY_MIYU;
			case YUZU:
				return Badges.Badge.MASTERY_YUZU;
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

	private static void initShiroko( Hero hero ) {
		AR_tier1 ar = new AR_tier1();

		(hero.belongings.weapon = ar).identify();
		hero.belongings.weapon.activate(hero);

		HPGrenade hpGrenade = new HPGrenade();
		hpGrenade.collect();

		Bicycle bicycle = new Bicycle();
		bicycle.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, ar);
		Dungeon.quickslot.setSlot(1, hpGrenade);
		Dungeon.quickslot.setSlot(2, bicycle);
		Dungeon.quickslot.setSlot(3, knives);

		new PotionOfStrength().identify();
		new ScrollOfMirrorImage().identify();
	}

	private static void initNoa( Hero hero ) {
		HG_tier1 hg = new HG_tier1();

		(hero.belongings.weapon = hg).identify();
		hero.belongings.weapon.activate(hero);

		FlashGrenade flashGrenade = new FlashGrenade();
		flashGrenade.collect();

		Laser laser = new Laser();
		laser.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, hg);
		Dungeon.quickslot.setSlot(1, laser);
		Dungeon.quickslot.setSlot(2, flashGrenade);
		Dungeon.quickslot.setSlot(3, knives);

		new PotionOfMindVision().identify();
		new ScrollOfMagicMapping().identify();
	}

	private static void initMiyu( Hero hero ) {
		SR_tier1 sr = new SR_tier1();

		(hero.belongings.weapon = sr).identify();
		hero.belongings.weapon.activate(hero);

		RegrowGrenade regrowGrenade = new RegrowGrenade();
		regrowGrenade.collect();

		TrashBin trashBin = new TrashBin();
		trashBin.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, sr);
		Dungeon.quickslot.setSlot(1, trashBin);
		Dungeon.quickslot.setSlot(2, regrowGrenade);
		Dungeon.quickslot.setSlot(3, knives);

		new PotionOfStrength().identify();
		new ScrollOfUpgrade().identify();
	}

	private static void initYuzu( Hero hero ) {
		GL_tier1 gl = new GL_tier1();

		(hero.belongings.weapon = gl).identify();
		hero.belongings.weapon.activate(hero);

		RainbowGrenade rainbowGrenade = new RainbowGrenade();
		rainbowGrenade.collect();

		LevelAnalyzer analyzer = new LevelAnalyzer();
		analyzer.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, gl);
		Dungeon.quickslot.setSlot(1, analyzer);
		Dungeon.quickslot.setSlot(2, rainbowGrenade);
		Dungeon.quickslot.setSlot(3, knives);

		new PotionOfLiquidFlame().identify();
		new ScrollOfMagicMapping().identify();
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
			case SHIROKO:
				return new ArmorAbility[]{new Shiroko_1(), new Shiroko_2(), new Shiroko_3()};
			case NOA:
				return new ArmorAbility[]{new Noa_1(), new Noa_2(), new Noa_3()};
			case MIYU:
				return new ArmorAbility[]{new Miyu_1(), new Miyu_2(), new Miyu_3()};
			case YUZU:
				return new ArmorAbility[]{new Yuzu_1(), new Yuzu_2(), new Yuzu_3()};
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
			case SHIROKO:
				return Assets.Sprites.SHIROKO;
			case NOA:
				return Assets.Sprites.NOA;
			case MIYU:
				return Assets.Sprites.MIYU;
			case YUZU:
				return Assets.Sprites.YUZU;
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
			case SHIROKO:
				return Assets.Splashes.SHIROKO;
			case NOA:
				return Assets.Splashes.NOA;
			case MIYU:
				return Assets.Splashes.MIYU;
			case YUZU:
				return Assets.Splashes.YUZU;
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
			case SHIROKO:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_SHIROKO);
			case NOA:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_NOA);
			case MIYU:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MIYU);
			case YUZU:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_YUZU);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
