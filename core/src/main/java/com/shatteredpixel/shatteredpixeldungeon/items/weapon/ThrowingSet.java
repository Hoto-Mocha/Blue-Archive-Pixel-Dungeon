/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlastMissile;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Claymore;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ThrowingSet extends Weapon {
	
	public static final String AC_SHOOT		= "SHOOT";
	public static final String AC_KUNAI		= "KUNAI";

	protected float amount;
	protected int max_amount;
	public static final String TXT_STATUS = "%d/%d";

	{
		max_amount = 3;
		amount = max_amount;

		image = ItemSpriteSheet.THROWING_SET;
		
		defaultAction = AC_SHOOT;
		usesTargeting = true;

		levelKnown = true;
		
		unique = true;
		bones = false;
	}

	public void use() {
		this.amount--;
		amount = Math.max(0, amount);
	}

	public int maxAmount() { //최대 장탄수
		int max = max_amount;

		max += this.buffedLvl()/2;

		return max;
	}

	public void reload() {
		amount += 0.25f*(1+0.5f*Dungeon.hero.pointsInTalent(Talent.SECRET_WEAPON));
		amount = Math.min(amount, maxAmount());
		Item.updateQuickslot();
	}

	@Override
	public String status() { //아이템 칸 오른쪽 위에 나타내는 글자
		return Messages.format(TXT_STATUS, (int)Math.floor(amount), maxAmount()); //TXT_STATUS 형식(%d/%d)으로, amount, maxAmount() 변수를 순서대로 %d부분에 출력
	}
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.remove(AC_EQUIP);
		actions.add(AC_SHOOT);
		actions.add(AC_KUNAI);
		return actions;
	}
	
	@Override
	public void execute(Hero hero, String action) {
		
		super.execute(hero, action);
		
		if (action.equals(AC_SHOOT)) {
			curUser = hero;
			curItem = this;
			if (amount >= 1 && ((Hero) curUser).justMoved) {
				GameScene.selectCell(shurikenShooter);
			} else {
				GameScene.selectCell(kunaiShooter);
			}
		} else if (action.equals(AC_KUNAI)) {
			curUser = hero;
			curItem = this;
			GameScene.selectCell(kunaiShooter);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		return super.proc(attacker, defender, damage);
	}

	@Override
	public String info() {
		String info = desc();
		
		info += "\n\n" + Messages.get( ThrowingSet.class, "stats",
				Math.round(augment.damageFactor(min())),
				Math.round(augment.damageFactor(max())),
				STRReq());
		
		if (STRReq() > Dungeon.hero.STR()) {
			info += " " + Messages.get(Weapon.class, "too_heavy");
		} else if (Dungeon.hero.STR() > STRReq()){
			info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
		}
		
		switch (augment) {
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
			info += " " + enchantment.desc();
		} else if (enchantHardened){
			info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}
		
		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");
		
		return info;
	}
	
	@Override
	public int STRReq(int lvl) {
		return STRReq(1, lvl); //tier 1
	}
	
	@Override
	public int min(int lvl) {
		int dmg = 1 + Dungeon.hero.lvl/5
				+ RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
				+ (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0);
		return Math.max(0, dmg);
	}
	
	@Override
	public int max(int lvl) {
		int dmg = 6 + (int)(Dungeon.hero.lvl/2.5f)
				+ 2*RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
				+ (curseInfusionBonus ? 2 + Dungeon.hero.lvl/15 : 0);
		return Math.max(0, dmg);
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return knockShuriken().targetingPos(user, dst);
	}
	
	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll(owner));
		
		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
			}
		}
		
		return damage;
	}
	
	@Override
	protected float baseDelay(Char owner) {
		return super.baseDelay(owner);
	}

	@Override
	protected float speedMultiplier(Char owner) {
		float speed = super.speedMultiplier(owner);
		return speed;
	}

	@Override
	public int level() {
		int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
		if (curseInfusionBonus) level += 1 + level/6;
		return level;
	}

	@Override
	public int buffedLvl() {
		//level isn't affected by buffs/debuffs
		return level();
	}
	
	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	public SpiritShuriken knockShuriken(){
		return new SpiritShuriken();
	}
	
	public class SpiritShuriken extends MissileWeapon {
		
		{
			image = ItemSpriteSheet.SPIRIT_SHURIKEN;

			hitSound = Assets.Sounds.HIT_STAB;
			hitSoundPitch = 1.2f;
		}

		@Override
		public Emitter emitter() {
			return super.emitter();
		}

		@Override
		public int damageRoll(Char owner) {
			return ThrowingSet.this.damageRoll(owner);
		}
		
		@Override
		public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
			return ThrowingSet.this.hasEnchant(type, owner);
		}
		
		@Override
		public int proc(Char attacker, Char defender, int damage) {
			if (attacker == Dungeon.hero && Dungeon.hero.subClass == HeroSubClass.IZUNA_EX_EXPLOSION && defender instanceof Mob && !(defender instanceof NPC) && defender.isAlive()) {
				Buff.affect(Dungeon.hero, BlastMissile.class).hit((Mob)defender);
			}
			return ThrowingSet.this.proc(attacker, defender, damage);
		}

		@Override
		public float delayFactor(Char owner) {
			return 0;
		}
		
		@Override
		public float accuracyFactor(Char owner, Char target) {
			return super.accuracyFactor(owner, target);
		}
		
		@Override
		public int STRReq(int lvl) {
			return ThrowingSet.this.STRReq();
		}

		@Override
		protected void onThrow( int cell ) {
			Char enemy = Actor.findChar( cell );
			if (enemy == null || enemy == curUser) {
				parent = null;
				Splash.at( cell, 0xCC99FFFF, 1 );
			} else {
				if (!curUser.shoot( enemy, this )) {
					Splash.at(cell, 0xCC99FFFF, 1);
				}
			}
			ThrowingSet.this.use();
		}

		@Override
		public void cast(final Hero user, final int dst) {
			super.cast(user, dst);
		}
	}
	
	private CellSelector.Listener shurikenShooter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				knockShuriken().cast(curUser, target);
			}
		}
		@Override
		public String prompt() {
			return Messages.get(ThrowingSet.class, "prompt");
		}
	};

	public SpiritKunai knockKunai(){
		return new SpiritKunai();
	}

	public class SpiritKunai extends MissileWeapon {

		{
			image = ItemSpriteSheet.SPIRIT_KUNAI;

			hitSound = Assets.Sounds.HIT_STAB;
			hitSoundPitch = 1.1f;
		}

		private Char enemy;

		@Override
		public Emitter emitter() {
			return super.emitter();
		}

		@Override
		public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
			return ThrowingSet.this.hasEnchant(type, owner);
		}

		@Override
		public int proc(Char attacker, Char defender, int damage) {
			if (attacker == Dungeon.hero && Dungeon.hero.subClass == HeroSubClass.IZUNA_EX_EXPLOSION && defender instanceof Mob && !(defender instanceof NPC) && defender.isAlive()) {
				Buff.affect(Dungeon.hero, BlastMissile.class).hit((Mob)defender);
			}
			return ThrowingSet.this.proc(attacker, defender, damage);
		}

		@Override
		public float accuracyFactor(Char owner, Char target) {
			return super.accuracyFactor(owner, target);
		}

		@Override
		public int STRReq(int lvl) {
			return ThrowingSet.this.STRReq();
		}

		@Override
		protected void onThrow(int cell) {
			enemy = Actor.findChar(cell);
			if (enemy == null || enemy == curUser) {
				parent = null;
				Splash.at( cell, 0xCC99FFFF, 1 );
			} else {
				if (!curUser.shoot( enemy, this )) {
					Splash.at(cell, 0xCC99FFFF, 1);
				}
			}
		}

		@Override
		public int damageRoll(Char owner) {
			if (owner instanceof Hero) {
				Hero hero = (Hero)owner;
				if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
					//deals 60% toward max to max on surprise, instead of min to max.
					int diff = ThrowingSet.this.max() - ThrowingSet.this.min();
					int damage = augment.damageFactor(Random.NormalIntRange(
							ThrowingSet.this.min() + Math.round(diff*0.6f),
							ThrowingSet.this.max()));
					int exStr = hero.STR() - STRReq();
					if (exStr > 0) {
						damage += Random.IntRange(0, exStr);
					}
					return damage;
				} else {
					int damage = augment.damageFactor(Random.NormalIntRange(
							ThrowingSet.this.min(),
							ThrowingSet.this.max()));
					int exStr = hero.STR() - STRReq();
					if (exStr > 0) {
						damage += Random.IntRange(0, exStr);
					}
					return damage;
				}
			}
			return super.damageRoll(owner);
		}

		@Override
		public float delayFactor(Char owner) {
			return ThrowingSet.this.delayFactor(owner);
		}

		@Override
		public void cast(final Hero user, final int dst) {
			super.cast(user, dst);
		}
	}

	private CellSelector.Listener kunaiShooter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				knockKunai().cast(curUser, target);
			}
		}
		@Override
		public String prompt() {
			return Messages.get(ThrowingSet.class, "prompt");
		}
	};
}
