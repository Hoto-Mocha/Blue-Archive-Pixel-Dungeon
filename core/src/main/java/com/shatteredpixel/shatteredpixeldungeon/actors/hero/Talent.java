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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Bicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.AR;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.MG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.SG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.SMG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public enum Talent {
	//Aris T1
	FOR_LIGHT(0), ROBOTS_INTUITION(1), TEST_SUBJECT(2), ACCEL_ENERGY(3),
	//Aris T2
	IRON_STOMACH(4), RESTORED_WILLPOWER(5), RUNIC_TRANSFERENCE(6), EMPOWERING_MAGIC(7), BALANCE_COLLAPSE(8),
	//Aris T3
	ROBOT_CLEANER(9, 3), STRONGMAN(10, 3),
	//SuperNova T3
	ENERGY_DRAIN(11, 3), PIEZOELECTRICITY(12, 3), HOLYSWORD(13, 3),
	//OverCharge T3
	INCREASING_OUTPUT(14, 3), OVERCHARGE(15, 3), CHARGE_ACCEL(16, 3),
	//Aris Ability 1
	ARIS_ABIL_11(17, 4), ARIS_ABIL_12(18, 4), ARIS_ABIL_13(19, 4),
	//Aris Ability 2
	ARIS_ABIL_21(20, 4), ARIS_ABIL_22(21, 4), ARIS_ABIL_23(22, 4),
	//Aris Ability 3
	ARIS_ABIL_31(23, 4), ARIS_ABIL_32(24, 4), ARIS_ABIL_33(25, 4),

	//Nonomi T1
	RESTING(32), MG_INTUITION(33), MAX_HEALTH(34), NO_WAY(35),
	//Nonomi T2
	RELOADING_MEAL(36), GOLD_CARD(37), LARGE_MAGAZINE(38), SURPRISE(39), MG_MASTER(40),
	//Nonomi T3
	MG_FAST_RELOAD(41, 3), ALLY_WARP(42, 3),
	//Riot T3
	RIOT_1(43, 3), RIOT_2(44, 3), RIOT_3(45, 3),
	//ShootAll T3
	SHOOTALL_1(46, 3), SHOOTALL_2(47, 3), SHOOTALL_3(48, 3),
	//Nonomi Ability 1
	NONOMI_ABIL_11(49, 4), NONOMI_ABIL_12(50, 4), NONOMI_ABIL_13(51, 4),
	//Nonomi Ability 2
	NONOMI_ABIL_21(52, 4), NONOMI_ABIL_22(53, 4), NONOMI_ABIL_23(54, 4),
	//Nonomi Ability 3
	NONOMI_ABIL_31(55, 4), NONOMI_ABIL_32(56, 4), NONOMI_ABIL_33(57, 4),

	//Miyako T1
	PLATE_ADD(64), SMG_INTUITION(65), SUPPLY(66), DISTURB(67),
	//Miyako T2
	TACTICAL_MEAL(68), INTELLIGENCE(69), CQC(70), TACTICAL_MOVE(71), SMG_MASTER(72),
	//Miyako T3
	SMG_FAST_RELOAD(73, 3), RABBIT_OPEN_UP(74, 3),
	//StunDrone T3
	STUNDRONE_1(75, 3), STUNDRONE_2(76, 3), STUNDRONE_3(77, 3),
	//DroneStrike T3
	DRONESTRIKE_1(78, 3), DRONESTRIKE_2(79, 3), DRONESTRIKE_3(80, 3),
	//Miyako Ability 1
	MIYAKO_ABIL_11(81, 4), MIYAKO_ABIL_12(82, 4), MIYAKO_ABIL_13(83, 4),
	//Miyako Ability 2
	MIYAKO_ABIL_21(84, 4), MIYAKO_ABIL_22(85, 4), MIYAKO_ABIL_23(86, 4),
	//Miyako Ability 3
	MIYAKO_ABIL_31(87, 4), MIYAKO_ABIL_32(88, 4), MIYAKO_ABIL_33(89, 4),

	//Hoshino T1
	EMERGENCY_HEALING(96), SG_INTUITION(97), ADDITIONAL_SHOT(98), RELOADING_SHIELD(99),
	//Hoshino T2
	SLEEPING_MEAL(100), QUICK_RETREAT(101), FORESIGHT_EYES(102), MAGIC_SHIELD(103), SG_MASTER(104),
	//Hoshino T3
	SG_FAST_RELOAD(105, 3), HARD_SHIELD(106, 3),
	//TacticalPress T3
	TACTICAL_PRESS_1(107, 3), TACTICAL_PRESS_2(108, 3), TACTICAL_PRESS_3(109, 3),
	//TacticalShield T3
	TACTICAL_SHIELD_1(110, 3), TACTICAL_SHIELD_2(111, 3), TACTICAL_SHIELD_3(112, 3),
	//Hoshino Ability 1
	HOSHINO_ABIL_11(113, 4), HOSHINO_ABIL_12(114, 4), HOSHINO_ABIL_13(115, 4),
	//Hoshino Ability 2
	HOSHINO_ABIL_21(116, 4), HOSHINO_ABIL_22(117, 4), HOSHINO_ABIL_23(118, 4),
	//Hoshino Ability 3
	HOSHINO_ABIL_31(119, 4), HOSHINO_ABIL_32(120, 4), HOSHINO_ABIL_33(121, 4),

	//Shiroko T1
	BICYCLE_CHARGE(128), AR_INTUITION(129), ENHANCED_EXPLODE(130), DEFENSIVE_FEEDBACK(131),
	//Shiroko T2
	SPEEDY_MEAL(132), INHERENT_WILDNESS(133), ELECTRIC_BICYCLE(134), RAPID_SHOOTING(135), AR_MASTER(136),
	//Shiroko T3
	AR_FAST_RELOAD(137, 3), SHOOTING_WEAKNESS(138, 3),
	// ElementalBullet T3
	ELEMENTAL_BULLET_1(139, 3), ELEMENTAL_BULLET_2(140, 3), ELEMENTAL_BULLET_3(141, 3),
	// ProfessionalRide T3
	PROFESSIONAL_RIDE_1(142, 3), PROFESSIONAL_RIDE_2(143, 3), PROFESSIONAL_RIDE_3(144, 3),
	//Shiroko Ability 1
	SHIROKO_ABIL_11(145, 4), SHIROKO_ABIL_12(146, 4), SHIROKO_ABIL_13(147, 4),
	//Shiroko Ability 2
	SHIROKO_ABIL_21(148, 4), SHIROKO_ABIL_22(149, 4), SHIROKO_ABIL_23(150, 4),
	//Shiroko Ability 3
	SHIROKO_ABIL_31(151, 4), SHIROKO_ABIL_32(152, 4), SHIROKO_ABIL_33(153, 4),













	//Rogue T1
	CACHED_RATIONS(-1), THIEFS_INTUITION(-1), SUCKER_PUNCH(-1), PROTECTIVE_SHADOWS(-1),
	//Rogue T2
	MYSTICAL_MEAL(-1), MYSTICAL_UPGRADE(-1), WIDE_SEARCH(-1), SILENT_STEPS(-1), ROGUES_FORESIGHT(-1),
	//Rogue T3
	ENHANCED_RINGS(-1, 3), LIGHT_CLOAK(-1, 3),
	//Assassin T3
	ENHANCED_LETHALITY(-1, 3), ASSASSINS_REACH(-1, 3), BOUNTY_HUNTER(-1, 3),
	//Freerunner T3
	EVASIVE_ARMOR(-1, 3), PROJECTILE_MOMENTUM(-1, 3), SPEEDY_STEALTH(-1, 3),
	//Smoke Bomb T4
	HASTY_RETREAT(-1, 4), BODY_REPLACEMENT(-1, 4), SHADOW_STEP(-1, 4),
	//Death Mark T4
	FEAR_THE_REAPER(-1, 4), DEATHLY_DURABILITY(-1, 4), DOUBLE_MARK(-1, 4),
	//Shadow Clone T4
	SHADOW_BLADE(-1, 4), CLONED_ARMOR(-1, 4), PERFECT_COPY(-1, 4),

	//Huntress T1
	NATURES_BOUNTY(-1), SURVIVALISTS_INTUITION(-1), FOLLOWUP_STRIKE(-1), NATURES_AID(-1),
	//Huntress T2
	INVIGORATING_MEAL(-1), RESTORED_NATURE(-1), REJUVENATING_STEPS(-1), HEIGHTENED_SENSES(-1), DURABLE_PROJECTILES(-1),
	//Huntress T3
	POINT_BLANK(-1, 3), SEER_SHOT(-1, 3),
	//Sniper T3
	FARSIGHT(-1, 3), SHARED_ENCHANTMENT(-1, 3), SHARED_UPGRADES(-1, 3),
	//Warden T3
	DURABLE_TIPS(-1, 3), BARKSKIN(-1, 3), SHIELDING_DEW(-1, 3),
	//Spectral Blades T4
	FAN_OF_BLADES(-1, 4), PROJECTING_BLADES(-1, 4), SPIRIT_BLADES(-1, 4),
	//Natures Power T4
	GROWING_POWER(-1, 4), NATURES_WRATH(-1, 4), WILD_MOMENTUM(-1, 4),
	//Spirit Hawk T4
	EAGLE_EYE(-1, 4), GO_FOR_THE_EYES(-1, 4), SWIFT_SPIRIT(-1, 4),

	//Duelist T1
	STRENGTHENING_MEAL(-1), ADVENTURERS_INTUITION(-1), PATIENT_STRIKE(-1), AGGRESSIVE_BARRIER(-1),
	//Duelist T2
	FOCUSED_MEAL(-1), RESTORED_AGILITY(-1), WEAPON_RECHARGING(-1), LETHAL_HASTE(-1), SWIFT_EQUIP(-1),
	//Duelist T3
	PRECISE_ASSAULT(-1, 3), DEADLY_FOLLOWUP(-1, 3),
	//Champion T3
	SECONDARY_CHARGE(-1, 3), TWIN_UPGRADES(-1, 3), COMBINED_LETHALITY(-1, 3),
	//Monk T3
	UNENCUMBERED_SPIRIT(-1, 3), MONASTIC_VIGOR(-1, 3), COMBINED_ENERGY(-1, 3),
	//Challenge T4
	CLOSE_THE_GAP(-1, 4), INVIGORATING_VICTORY(-1, 4), ELIMINATION_MATCH(-1, 4),
	//Elemental Strike T4
	ELEMENTAL_REACH(-1, 4), STRIKING_FORCE(-1, 4), DIRECTED_POWER(-1, 4),
	//Duelist A3 T4
	FEIGNED_RETREAT(-1, 4), EXPOSE_WEAKNESS(-1, 4), COUNTER_ABILITY(-1, 4),

	//universal T4
	HEROIC_ENERGY(26, 4), //See icon() and title() for special logic for this one
	//Ratmogrify T4
	RATSISTANCE(215, 4), RATLOMACY(216, 4), RATFORCEMENTS(217, 4);

	public static class NoWayCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 30); }
	};
	public static class EmergencyHealingCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xFF9AB0); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class TacticalInvisibilityTracker extends Buff{};
	public static class EmpoweringMagic extends FlavourBuff{
		public int icon() { return BuffIndicator.UPGRADE; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, (5 - visualcooldown()) / 5); }
	};
	public static class StrikingWaveTracker extends FlavourBuff{};
	public static class ProtectiveShadowsTracker extends Buff {
		float barrierInc = 0.5f;

		@Override
		public boolean act() {
			//barrier every 2/1 turns, to a max of 3/5
			if (((Hero)target).hasTalent(Talent.PROTECTIVE_SHADOWS) && target.invisible > 0){
				Barrier barrier = Buff.affect(target, Barrier.class);
				if (barrier.shielding() < 1 + 2*((Hero)target).pointsInTalent(Talent.PROTECTIVE_SHADOWS)) {
					barrierInc += 0.5f * ((Hero) target).pointsInTalent(Talent.PROTECTIVE_SHADOWS);
				}
				if (barrierInc >= 1){
					barrierInc = 0;
					barrier.incShield(1);
				} else {
					barrier.incShield(0); //resets barrier decay
				}
			} else {
				detach();
			}
			spend( TICK );
			return true;
		}

		private static final String BARRIER_INC = "barrier_inc";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( BARRIER_INC, barrierInc);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			barrierInc = bundle.getFloat( BARRIER_INC );
		}
	}

	public static class BountyHunterTracker extends FlavourBuff{};
	public static class RejuvenatingStepsCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.35f, 0.15f); }
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / (15 - 5*Dungeon.hero.pointsInTalent(REJUVENATING_STEPS)), 1); }
	};
	public static class RejuvenatingStepsFurrow extends CounterBuff{{revivePersists = true;}};
	public static class SeerShotCooldown extends FlavourBuff{
		public int icon() { return target.buff(RevealedArea.class) != null ? BuffIndicator.NONE : BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.7f, 0.4f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	};
	public static class SpiritBladesTracker extends FlavourBuff{};
	public static class PatientStrikeTracker extends Buff {
		public int pos;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		@Override
		public boolean act() {
			if (pos != target.pos) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
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
	};
	public static class AggressiveBarrierCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class RestoredAgilityTracker extends FlavourBuff{};
	public static class LethalHasteCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 100); }
	};
	public static class SwiftEquipCooldown extends FlavourBuff{
		public boolean secondUse;
		public boolean hasSecondUse(){
			return secondUse && cooldown() > 14f;
		}

		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) {
			if (hasSecondUse()) icon.hardlight(0.85f, 0f, 1.0f);
			else                icon.hardlight(0.35f, 0f, 0.7f);
		}
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / 20f, 1); }

		private static final String SECOND_USE = "second_use";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(SECOND_USE, secondUse);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			secondUse = bundle.getBoolean(SECOND_USE);
		}
	};
	public static class DeadlyFollowupTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	}
	public static class PreciseAssaultTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0.0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	public static class CombinedLethalityAbilityTracker extends FlavourBuff{
		public MeleeWeapon weapon;
	};
	public static class CombinedLethalityTriggerTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.CORRUPT; }
		public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.15f, 0.6f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	public static class CombinedEnergyAbilityTracker extends FlavourBuff{
		public int energySpent = -1;
		public boolean wepAbilUsed = false;
	}
	public static class CounterAbilityTacker extends FlavourBuff{};

	public static class ScanCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0xFFC4E5 ); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 60-10*Dungeon.hero.pointsInTalent(Talent.DRONESTRIKE_2))); }
	};

	public static class ShootingWeaknessCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0xC7C4C9 ); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 40-10*Dungeon.hero.pointsInTalent(Talent.SHOOTING_WEAKNESS))); }
	};

	int icon;
	int maxPoints;

	// tiers 1/2/3/4 start at levels 2/7/13/21
	public static int[] tierLevelThresholds = new int[]{0, 2, 7, 13, 21, 31};

	Talent( int icon ){
		this(icon, 2);
	}

	Talent( int icon, int maxPoints ){
		this.icon = icon;
		this.maxPoints = maxPoints;
	}

	public int icon(){
		if (this == HEROIC_ENERGY){
			if (Ratmogrify.useRatroicEnergy){
				return 218;
			}
			HeroClass cls = Dungeon.hero != null ? Dungeon.hero.heroClass : GamesInProgress.selectedClass;
			switch (cls){
				case ARIS: default:
					return 26;
				case NONOMI:
					return 58;
				case MIYAKO:
					return 90;
				case HOSHINO:
					return 122;
				case SHIROKO:
					return 154;
			}
		} else {
			return icon;
		}
	}

	public int maxPoints(){
		return maxPoints;
	}

	public String title(){
		if (this == HEROIC_ENERGY && Ratmogrify.useRatroicEnergy){
			return Messages.get(this, name() + ".rat_title");
		}
		return Messages.get(this, name() + ".title");
	}

	public final String desc(){
		return desc(false);
	}

	public String desc(boolean metamorphed){
		if (metamorphed){
			String metaDesc = Messages.get(this, name() + ".meta_desc");
			if (!metaDesc.equals(Messages.NO_TEXT_FOUND)){
				return Messages.get(this, name() + ".desc") + "\n\n" + metaDesc;
			}
		}
		return Messages.get(this, name() + ".desc");
	}

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		//for metamorphosis
		if (talent == ROBOTS_INTUITION && hero.pointsInTalent(ROBOTS_INTUITION) == 2){
			if (hero.belongings.armor() != null)  hero.belongings.armor.identify();
		}
		if (talent == MAX_HEALTH) {
			hero.updateHT(true);
		}
		if (talent == LARGE_MAGAZINE) {
			Item.updateQuickslot();
		}



		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.identify();
			if (hero.belongings.misc instanceof Ring) hero.belongings.misc.identify();
			for (Item item : Dungeon.hero.belongings){
				if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
			if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
		}
		if (talent == ADVENTURERS_INTUITION && hero.pointsInTalent(ADVENTURERS_INTUITION) == 2){
			if (hero.belongings.weapon() != null) hero.belongings.weapon().identify();
		}

		if (talent == PROTECTIVE_SHADOWS && hero.invisible > 0){
			Buff.affect(hero, Talent.ProtectiveShadowsTracker.class);
		}

		if (talent == INTELLIGENCE || talent == FARSIGHT){
			Dungeon.observe();
		}

		if (talent == SECONDARY_CHARGE || talent == TWIN_UPGRADES){
			Item.updateQuickslot();
		}

		if (talent == UNENCUMBERED_SPIRIT && hero.pointsInTalent(talent) == 3){
			Item toGive = new ClothArmor().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
			toGive = new Gloves().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
		}

		if (talent == HOLYSWORD && hero.pointsInTalent(talent) == 3) {
			Item.updateQuickslot();
		}
	}

	public static class CachedRationsDropped extends CounterBuff{{revivePersists = true;}};
	public static class NatureBerriesAvailable extends CounterBuff{{revivePersists = true;}}; //for pre-1.3.0 saves
	public static class NatureBerriesDropped extends CounterBuff{{revivePersists = true;}};

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		if (hero.hasTalent(RESTING)) {
			//3/5 HP healed, when hero is below 25% health
			if (hero.HP <= hero.HT / 4) {
				hero.HP = Math.min(hero.HP + 1 + 2 * hero.pointsInTalent(RESTING), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + hero.pointsInTalent(RESTING));
				//2/3 HP healed, when hero is below 50% health
			} else if (hero.HP <= hero.HT / 2) {
				hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(RESTING), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(RESTING));
			}
		}
		if (hero.hasTalent(IRON_STOMACH)){
			if (hero.cooldown() > 0) {
				Buff.affect(hero, WarriorFoodImmunity.class, hero.cooldown());
			}
		}
		if (hero.hasTalent(RELOADING_MEAL)) {
			if (hero.belongings.weapon instanceof Gun) {
				Gun wep = (Gun)hero.belongings.weapon;
				wep.manualReload(hero.pointsInTalent(RELOADING_MEAL), true);
			}
		}
		if (hero.hasTalent(TACTICAL_MEAL)) {
			Buff.affect(hero, Barrier.class).incShield(Math.round(hero.HT/10f*hero.pointsInTalent(Talent.TACTICAL_MEAL)));
		}
		if (hero.hasTalent(SLEEPING_MEAL)) {
			Buff.affect(hero, Drowsy.class).set(90-20*hero.pointsInTalent(Talent.SLEEPING_MEAL));
		}
		if (hero.hasTalent(SPEEDY_MEAL)) {
			Buff.affect(hero, Stamina.class, 1+7*hero.pointsInTalent(Talent.SPEEDY_MEAL));
		}

		if (hero.hasTalent(MYSTICAL_MEAL)){
			//3/5 turns of recharging
			ArtifactRecharge buff = Buff.affect( hero, ArtifactRecharge.class);
			if (buff.left() < 1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))){
				Buff.affect( hero, ArtifactRecharge.class).set(1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))).ignoreHornOfPlenty = foodSource instanceof HornOfPlenty;
			}
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, SpellSprite.CHARGE, 0, 1, 1);
		}
		if (hero.hasTalent(INVIGORATING_MEAL)){
			//effectively 1/2 turns of haste
			Buff.prolong( hero, Haste.class, 0.67f+hero.pointsInTalent(INVIGORATING_MEAL));
		}
		if (hero.hasTalent(STRENGTHENING_MEAL)){
			//3 bonus physical damage for next 2/3 attacks
			Buff.affect( hero, PhysicalEmpower.class).set(3, 1 + hero.pointsInTalent(STRENGTHENING_MEAL));
		}
		if (hero.hasTalent(BICYCLE_CHARGE)) {
			Bicycle bicycle = hero.belongings.getItem(Bicycle.class);
			if (bicycle != null) {
				bicycle.chargeUp(hero.pointsInTalent(BICYCLE_CHARGE)/8f);
				ScrollOfRecharging.charge(hero);
			}
		}
//		if (hero.hasTalent(FOCUSED_MEAL)){
//			if (hero.heroClass == HeroClass.DUELIST){
//				//1/1.5 charge for the duelist
//				Buff.affect( hero, MeleeWeapon.Charger.class ).gainCharge(0.5f*(hero.pointsInTalent(FOCUSED_MEAL)+1));
//			} else {
//				// lvl/3 / lvl/2 bonus dmg on next hit for other classes
//				Buff.affect( hero, PhysicalEmpower.class).set(Math.round(hero.lvl / (4f - hero.pointsInTalent(FOCUSED_MEAL))), 1);
//			}
//		}
	}

	public static class WarriorFoodImmunity extends FlavourBuff{
		{ actPriority = HERO_PRIO+1; }
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		// 1.75x/2.5x speed with Huntress talent
		float factor = 1f + 0.75f*hero.pointsInTalent(SURVIVALISTS_INTUITION);

		// Affected by both Warrior(1.75x/2.5x) and Duelist(2.5x/inst.) talents
		if (item instanceof MeleeWeapon){
			factor *= 1f + 1.5f*hero.pointsInTalent(ADVENTURERS_INTUITION); //instant at +2 (see onItemEquipped)
			factor *= 1f + 1.5f*hero.pointsInTalent(ROBOTS_INTUITION);
		}
		// Affected by both Warrior(2.5x/inst.) and Duelist(1.75x/2.5x) talents
		if (item instanceof Armor){
			factor *= 1f + 0.75f*hero.pointsInTalent(ADVENTURERS_INTUITION);
			factor *= 1f + 1.5f*hero.pointsInTalent(ROBOTS_INTUITION); //instant at +2 (see onItemEquipped)
		}
		// 3x/instant for Mage (see Wand.wandUsed())
//		if (item instanceof Wand){
//			factor *= 1f + 2.0f*hero.pointsInTalent(SCHOLARS_INTUITION);
//		}
		// 2x/instant for Rogue (see onItemEqupped), also id's type on equip/on pickup
		if (item instanceof Ring){
			factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
		}
		return factor;
	}

	public static void onHealingPotionUsed( Hero hero ){
		if (hero.hasTalent(RESTORED_WILLPOWER)){
			if (hero.heroClass == HeroClass.ARIS) {
				BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
				if (shield != null) {
					int shieldToGive = Math.round(shield.maxShield() * 0.33f * (1 + hero.pointsInTalent(RESTORED_WILLPOWER)));
					shield.supercharge(shieldToGive);
				}
			} else {
				int shieldToGive = Math.round( hero.HT * (0.025f * (1+hero.pointsInTalent(RESTORED_WILLPOWER))));
				Buff.affect(hero, Barrier.class).setShield(shieldToGive);
			}
		}
		if (hero.hasTalent(RESTORED_NATURE)){
			ArrayList<Integer> grassCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				grassCells.add(hero.pos+i);
			}
			Random.shuffle(grassCells);
			for (int cell : grassCells){
				Char ch = Actor.findChar(cell);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY){
					Buff.affect(ch, Roots.class, 1f + hero.pointsInTalent(RESTORED_NATURE));
				}
				if (Dungeon.level.map[cell] == Terrain.EMPTY ||
						Dungeon.level.map[cell] == Terrain.EMBERS ||
						Dungeon.level.map[cell] == Terrain.EMPTY_DECO){
					Level.set(cell, Terrain.GRASS);
					GameScene.updateMap(cell);
				}
				CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}
			if (hero.pointsInTalent(RESTORED_NATURE) == 1){
				grassCells.remove(0);
				grassCells.remove(0);
				grassCells.remove(0);
			}
			for (int cell : grassCells){
				int t = Dungeon.level.map[cell];
				if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
						|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
						&& Dungeon.level.plants.get(cell) == null){
					Level.set(cell, Terrain.HIGH_GRASS);
					GameScene.updateMap(cell);
				}
			}
			Dungeon.observe();
		}
		if (hero.hasTalent(RESTORED_AGILITY)){
			Buff.prolong(hero, RestoredAgilityTracker.class, hero.cooldown());
		}
	}

	public static void onUpgradeScrollUsed( Hero hero ){

	}

	public static void onArtifactUsed( Hero hero ){
		if (hero.hasTalent(ENHANCED_RINGS)){
			Buff.prolong(hero, EnhancedRings.class, 3f*hero.pointsInTalent(ENHANCED_RINGS));
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.pointsInTalent(ROBOTS_INTUITION) == 2 && (item instanceof Armor || item instanceof MeleeWeapon)){
			item.identify();
		}
		if (hero.pointsInTalent(MG_INTUITION) == 2 && item instanceof MG){
			item.identify();
		}
		if (hero.pointsInTalent(SMG_INTUITION) == 2 && item instanceof SMG){
			item.identify();
		}
		if (hero.pointsInTalent(SG_INTUITION) == 2 && item instanceof SG){
			item.identify();
		}
		if (hero.pointsInTalent(AR_INTUITION) == 2 && item instanceof AR){
			item.identify();
		}
		if (hero.hasTalent(THIEFS_INTUITION) && item instanceof Ring){
			if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
				item.identify();
			} else {
				((Ring) item).setKnown();
			}
		}
		if (hero.pointsInTalent(ADVENTURERS_INTUITION) == 2 && item instanceof Weapon){
			item.identify();
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.hasTalent(MG_INTUITION)){
			if (item instanceof MG) item.cursedKnown = true;
		}
		if (hero.hasTalent(SMG_INTUITION)){
			if (item instanceof SMG) item.cursedKnown = true;
		}
		if (hero.hasTalent(SG_INTUITION)){
			if (item instanceof SG) item.cursedKnown = true;
		}
		if (hero.hasTalent(AR_INTUITION)){
			if (item instanceof AR) item.cursedKnown = true;
		}
		if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (item instanceof Ring) ((Ring) item).setKnown();
		}
	}

	//note that IDing can happen in alchemy scene, so be careful with VFX here
	public static void onItemIdentified( Hero hero, Item item ){
		if (hero.hasTalent(TEST_SUBJECT)){
			//heal for 2/3 HP
			hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(TEST_SUBJECT), hero.HT);
			if (hero.sprite != null) {
				Emitter e = hero.sprite.emitter();
				if (e != null) e.burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(TEST_SUBJECT));
			}
		}
//		if (hero.hasTalent(TESTED_HYPOTHESIS)){
//			//2/3 turns of wand recharging
//			Buff.affect(hero, Recharging.class, 1f + hero.pointsInTalent(TESTED_HYPOTHESIS));
//			ScrollOfRecharging.charge(hero);
//		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(Talent.SURPRISE)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			dmg *= 1 + (0.05f*hero.pointsInTalent(Talent.SURPRISE));
		}

		if (hero.hasTalent(Talent.DISTURB)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)){
			Buff.prolong(enemy, Vertigo.class, 1+hero.pointsInTalent(Talent.DISTURB));
		}

		if (hero.hasTalent(Talent.SUCKER_PUNCH)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			dmg += Random.IntRange(hero.pointsInTalent(Talent.SUCKER_PUNCH) , 2);
			Buff.affect(enemy, SuckerPunchTracker.class);
		}

		if (hero.hasTalent(Talent.FOR_LIGHT)
				&& enemy instanceof Mob
				&& enemy.buff(ForLightTracker.class) == null){
			Buff.affect(enemy, Blindness.class, 1+hero.pointsInTalent(Talent.FOR_LIGHT));
			Buff.affect(enemy, ForLightTracker.class);
		}

		if (hero.hasTalent(Talent.FOLLOWUP_STRIKE) && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				Buff.prolong(hero, FollowupStrikeTracker.class, 5f).object = enemy.id();
			} else if (hero.buff(FollowupStrikeTracker.class) != null
					&& hero.buff(FollowupStrikeTracker.class).object == enemy.id()){
				dmg += 1 + hero.pointsInTalent(FOLLOWUP_STRIKE);
				hero.buff(FollowupStrikeTracker.class).detach();
			}
		}

		if (hero.buff(Talent.SpiritBladesTracker.class) != null
				&& Random.Int(10) < 3*hero.pointsInTalent(Talent.SPIRIT_BLADES)){
			SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
			if (bow != null) dmg = bow.proc( hero, enemy, dmg );
			hero.buff(Talent.SpiritBladesTracker.class).detach();
		}

		if (hero.hasTalent(PATIENT_STRIKE)){
			if (hero.buff(PatientStrikeTracker.class) != null
					&& !(hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				hero.buff(PatientStrikeTracker.class).detach();
				dmg += Random.IntRange(hero.pointsInTalent(Talent.PATIENT_STRIKE), 2);
			}
		}

		if (hero.hasTalent(DEADLY_FOLLOWUP) && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				if (!(hero.belongings.attackingWeapon() instanceof SpiritBow.SpiritArrow)) {
					Buff.prolong(hero, DeadlyFollowupTracker.class, 5f).object = enemy.id();
				}
			} else if (hero.buff(DeadlyFollowupTracker.class) != null
					&& hero.buff(DeadlyFollowupTracker.class).object == enemy.id()){
				dmg = Math.round(dmg * (1.0f + .08f*hero.pointsInTalent(DEADLY_FOLLOWUP)));
			}
		}

		return dmg;
	}

	public static class SuckerPunchTracker extends Buff{};
	public static class ForLightTracker extends Buff{};

	public static class FollowupStrikeTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.75f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	};

	public static final int MAX_TALENT_TIERS = 4;

	public static void initClassTalents( Hero hero ){
		initClassTalents( hero.heroClass, hero.talents, hero.metamorphedTalents );
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents){
		initClassTalents( cls, talents, new LinkedHashMap<>());
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents, LinkedHashMap<Talent, Talent> replacements ){
		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 1
		switch (cls){
			case ARIS: default:
				Collections.addAll(tierTalents, FOR_LIGHT, ROBOTS_INTUITION, TEST_SUBJECT, ACCEL_ENERGY);
				break;
			case NONOMI:
				Collections.addAll(tierTalents, RESTING, MG_INTUITION, MAX_HEALTH, NO_WAY);
				break;
			case MIYAKO:
				Collections.addAll(tierTalents, PLATE_ADD, SMG_INTUITION, SUPPLY, DISTURB);
				break;
			case HOSHINO:
				Collections.addAll(tierTalents, EMERGENCY_HEALING, SG_INTUITION, ADDITIONAL_SHOT, RELOADING_SHIELD);
				break;
			case SHIROKO:
				Collections.addAll(tierTalents, BICYCLE_CHARGE, AR_INTUITION, ENHANCED_EXPLODE, DEFENSIVE_FEEDBACK);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(0).put(talent, 0);
		}
		tierTalents.clear();

		//tier 2
		switch (cls){
			case ARIS: default:
				Collections.addAll(tierTalents, IRON_STOMACH, RESTORED_WILLPOWER, RUNIC_TRANSFERENCE, EMPOWERING_MAGIC, BALANCE_COLLAPSE);
				break;
			case NONOMI:
				Collections.addAll(tierTalents, RELOADING_MEAL, GOLD_CARD, LARGE_MAGAZINE, SURPRISE, MG_MASTER);
				break;
			case MIYAKO:
				Collections.addAll(tierTalents, TACTICAL_MEAL, INTELLIGENCE, CQC, TACTICAL_MOVE, SMG_MASTER);
				break;
			case HOSHINO:
				Collections.addAll(tierTalents, SLEEPING_MEAL, QUICK_RETREAT, FORESIGHT_EYES, MAGIC_SHIELD, SG_MASTER);
				break;
			case SHIROKO:
				Collections.addAll(tierTalents, SPEEDY_MEAL, INHERENT_WILDNESS, ELECTRIC_BICYCLE, RAPID_SHOOTING, AR_MASTER);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(1).put(talent, 0);
		}
		tierTalents.clear();

		//tier 3
		switch (cls){
			case ARIS: default:
				Collections.addAll(tierTalents, ROBOT_CLEANER, STRONGMAN);
				break;
			case NONOMI:
				Collections.addAll(tierTalents, MG_FAST_RELOAD, ALLY_WARP);
				break;
			case MIYAKO:
				Collections.addAll(tierTalents, SMG_FAST_RELOAD, RABBIT_OPEN_UP);
				break;
			case HOSHINO:
				Collections.addAll(tierTalents, SG_FAST_RELOAD, HARD_SHIELD);
				break;
			case SHIROKO:
				Collections.addAll(tierTalents, AR_FAST_RELOAD, SHOOTING_WEAKNESS);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

		//tier4
		//TBD
	}

	public static void initSubclassTalents( Hero hero ){
		initSubclassTalents( hero.subClass, hero.talents );
	}

	public static void initSubclassTalents( HeroSubClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (cls == HeroSubClass.NONE) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 3
		switch (cls){
			case ARIS_EX_SUPERNOVA: default:
				Collections.addAll(tierTalents, ENERGY_DRAIN, PIEZOELECTRICITY, HOLYSWORD);
				break;
			case ARIS_EX_CHARGE:
				Collections.addAll(tierTalents, INCREASING_OUTPUT, OVERCHARGE, CHARGE_ACCEL);
				break;
			case NONOMI_EX_RIOT:
				Collections.addAll(tierTalents, RIOT_1, RIOT_2, RIOT_3);
				break;
			case NONOMI_EX_SHOOTALL:
				Collections.addAll(tierTalents, SHOOTALL_1, SHOOTALL_2, SHOOTALL_3);
				break;
			case MIYAKO_EX_STUNDRONE:
				Collections.addAll(tierTalents, STUNDRONE_1, STUNDRONE_2, STUNDRONE_3);
				break;
			case MIYAKO_EX_DRONESTRIKE:
				Collections.addAll(tierTalents, DRONESTRIKE_1, DRONESTRIKE_2, DRONESTRIKE_3);
				break;
			case HOSHINO_EX_TACTICAL_PRESS:
				Collections.addAll(tierTalents, TACTICAL_PRESS_1, TACTICAL_PRESS_2, TACTICAL_PRESS_3);
				break;
			case HOSHINO_EX_TACTICAL_SHIELD:
				Collections.addAll(tierTalents, TACTICAL_SHIELD_1, TACTICAL_SHIELD_2, TACTICAL_SHIELD_3);
				break;
			case SHIROKO_EX_ELEMENTAL_BULLET:
				Collections.addAll(tierTalents, ELEMENTAL_BULLET_1, ELEMENTAL_BULLET_2, ELEMENTAL_BULLET_3);
				break;
			case SHIROKO_EX_PROFESSIONAL_RIDE:
				Collections.addAll(tierTalents, PROFESSIONAL_RIDE_1, PROFESSIONAL_RIDE_2, PROFESSIONAL_RIDE_3);
				break;
		}
		for (Talent talent : tierTalents){
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

	}

	public static void initArmorTalents( Hero hero ){
		initArmorTalents( hero.armorAbility, hero.talents);
	}

	public static void initArmorTalents(ArmorAbility abil, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (abil == null) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		for (Talent t : abil.talents()){
			talents.get(3).put(t, 0);
		}
	}

	private static final String TALENT_TIER = "talents_tier_";

	public static void storeTalentsInBundle( Bundle bundle, Hero hero ){
		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = new Bundle();

			for (Talent talent : tier.keySet()){
				if (tier.get(talent) > 0){
					tierBundle.put(talent.name(), tier.get(talent));
				}
				if (tierBundle.contains(talent.name())){
					tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
				}
			}
			bundle.put(TALENT_TIER+(i+1), tierBundle);
		}

		Bundle replacementsBundle = new Bundle();
		for (Talent t : hero.metamorphedTalents.keySet()){
			replacementsBundle.put(t.name(), hero.metamorphedTalents.get(t));
		}
		bundle.put("replacements", replacementsBundle);
	}

	private static final HashSet<String> removedTalents = new HashSet<>();
	static{
		//v1.4.0
		removedTalents.add("BERSERKING_STAMINA");
	}

	private static final HashMap<String, String> renamedTalents = new HashMap<>();
	static{
		//v2.0.0
		renamedTalents.put("ARMSMASTERS_INTUITION",     "ROBOTS_INTUITION");
		//v2.0.0 BETA
		renamedTalents.put("LIGHTLY_ARMED",             "UNENCUMBERED_SPIRIT");
		//v2.1.0
		renamedTalents.put("LIGHTWEIGHT_CHARGE",             "PRECISE_ASSAULT");
	}

	public static void restoreTalentsFromBundle( Bundle bundle, Hero hero ){
		if (bundle.contains("replacements")){
			Bundle replacements = bundle.getBundle("replacements");
			for (String key : replacements.getKeys()){
				String value = replacements.getString(key);
				if (renamedTalents.containsKey(key)) key = renamedTalents.get(key);
				if (renamedTalents.containsKey(value)) value = renamedTalents.get(value);
				if (!removedTalents.contains(key) && !removedTalents.contains(value)){
					try {
						hero.metamorphedTalents.put(Talent.valueOf(key), Talent.valueOf(value));
					} catch (Exception e) {
						ShatteredPixelDungeon.reportException(e);
					}
				}
			}
		}

		if (hero.heroClass != null)     initClassTalents(hero);
		if (hero.subClass != null)      initSubclassTalents(hero);
		if (hero.armorAbility != null)  initArmorTalents(hero);

		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = bundle.contains(TALENT_TIER+(i+1)) ? bundle.getBundle(TALENT_TIER+(i+1)) : null;

			if (tierBundle != null){
				for (String tName : tierBundle.getKeys()){
					int points = tierBundle.getInt(tName);
					if (renamedTalents.containsKey(tName)) tName = renamedTalents.get(tName);
					if (!removedTalents.contains(tName)) {
						try {
							Talent talent = Talent.valueOf(tName);
							if (tier.containsKey(talent)) {
								tier.put(talent, Math.min(points, talent.maxPoints()));
							}
						} catch (Exception e) {
							ShatteredPixelDungeon.reportException(e);
						}
					}
				}
			}
		}
	}

}
