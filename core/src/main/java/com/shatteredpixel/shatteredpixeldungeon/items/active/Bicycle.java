package com.shatteredpixel.shatteredpixeldungeon.items.active;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndProfessionalRide;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Bicycle extends Item {

	private float maxCharge = 60f;
	private float charge;

	private static final String TXT_STATUS = "%.1f%%";

	{
		image = ItemSpriteSheet.BICYCLE;
		levelKnown = true;

		defaultAction = AC_USE;
		usesTargeting = false;

		bones = false;
		unique = true;

		charge = maxCharge;
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
			if (hero.buff(BicycleCooldown.class) != null) {
				hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_ride_cooldown"));
				return;
			}
			if (charge > 1) {
				hero.sprite.operate(hero.pos);
				hero.spendAndNext(Actor.TICK);
				Buff buff = hero.buff(BicycleBuff.class);
				if (buff == null) {
					Buff.affect(hero, BicycleBuff.class);
					if (hero.buff(DriftBuff.class) != null) {
						hero.buff(DriftBuff.class).detach();
					}
				} else {
					buff.detach();
				}
			} else {
				hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_cannot_ride"));
			}
		}
	}

	private static final String CHARGE = "charge";
	private static final String MAX_CHARGE = "maxCharge";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHARGE, charge);
		bundle.put(MAX_CHARGE, maxCharge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		charge = bundle.getInt(CHARGE);
		maxCharge = bundle.getInt(MAX_CHARGE);
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
	public String status() { //아이템 칸 오른쪽 위에 나타내는 글자
		return Messages.format(TXT_STATUS, charge/maxCharge()*100); //TXT_STATUS 형식(%d/%d)으로, round, maxRound() 변수를 순서대로 %d부분에 출력
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

	public float maxCharge() {
		float max = maxCharge;
		max += 20*buffedLvl();
		return max;
	}

	public float charge() {
		return charge;
	}

	public void chargeUp(float amount) {
		if (hero.buff(DriftBuff.class) != null) {
			amount *= 3;
		}
		charge += amount;
		charge = Math.min(charge, maxCharge());
		updateQuickslot();
	}

	public void chargeUp() {
		chargeUp(0.5f*(1+hero.pointsInTalent(Talent.PROFESSIONAL_RIDE_1)/6f));
	}

	public void use() {
		use(1);
	}

	public void use(float amount) {
		charge -= amount;
		charge = Math.max(0, charge);
		if (charge < 1) {
			Buff buff = Dungeon.hero.buff(BicycleBuff.class);
			if (buff != null) buff.detach();
			Dungeon.hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_no_charge"));	//"충전량이 부족해."
		}
		updateQuickslot();
	}

	public float chargePercent() {
		return (maxCharge() - charge) / maxCharge();
	}

	public enum ProfessionalRideMove {
		SKID_MARK		(10, 0x00FF00),
		RIDING_RELOAD   (10, 0x00FF00),
		SLAM  			(15, 0xFFFF00),
		ACCELERATE  	(20, 0xFF0000),
		DRIFT   		(20, 0xFF0000);

		public float chargeReq;
		public int tintColor;

		ProfessionalRideMove(float chargeReq, int tintColor){
			this.chargeReq = chargeReq*(1-hero.pointsInTalent(Talent.PROFESSIONAL_RIDE_3)/6f); //충전량 소모량
			this.tintColor = tintColor;	//색칠
		}

		public String title(){
			return Messages.get(this, name() + ".name");
		}

		public String desc(){
			return Messages.get(this, name() + ".desc");
		}

	}

	public boolean canUseMove(ProfessionalRideMove move){
		if (move.chargeReq > charge()) {
			return false;
		}
		if (move == ProfessionalRideMove.ACCELERATE && hero.buff(AccelerationBuff.class) != null) {
			return false;
		}
		if (move == ProfessionalRideMove.DRIFT && hero.buff(DriftCooldown.class) != null) {
			return false;
		}

		return true;

	}

	public void useMove(ProfessionalRideMove move){
		switch (move) {
			case SKID_MARK:
				GameScene.selectCell(dasher);
				break;
			case RIDING_RELOAD:
				KindOfWeapon wep = hero.belongings.weapon;
				if (wep instanceof Gun) {
					((Gun) wep).reload();
					hero.spend(-((Gun) wep).reloadTime());
					Item.updateQuickslot();
					BuffIndicator.refreshHero();
				}
				use(move.chargeReq);
				break;
			case SLAM:
				GameScene.selectCell(thrower);
				break;
			case ACCELERATE:
				Sample.INSTANCE.play(Assets.Sounds.MISS);
				Buff.affect(hero, AccelerationBuff.class);
				hero.sprite.emitter().burst(Speck.factory(Speck.JET), 5);
				use(move.chargeReq);
				break;
			case DRIFT:
				if (hero.buff(BicycleBuff.class) != null) {
					hero.buff(BicycleBuff.class).detach();
				}
				if (hero.buff(BicycleCooldown.class) != null) {
					hero.buff(BicycleCooldown.class).detach();
				}
				Buff.affect(hero, DriftCooldown.class).set();
				Buff.affect(hero, DriftBuff.class, 19f);
				break;
		}
		ActionIndicator.refresh();
	}

	private static CellSelector.Listener dasher = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			Bicycle bicycle = hero.belongings.getItem(Bicycle.class);
			if (bicycle == null) {
				return;
			}

			if (target == null || target == -1 || (!Dungeon.level.visited[target] && !Dungeon.level.mapped[target])){
				return;
			}

			//chains cannot be used to go where it is impossible to walk to
			PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
			if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE){
				Dungeon.hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_dash_bad_position"));
				return;
			}

			if (hero.rooted){
				Dungeon.hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_rooted"));
				return;
			}

			int range = 3;

			if (Dungeon.level.distance(hero.pos, target) > range){
				Dungeon.hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + hero.heroClass.name() + "_dash_bad_position"));	//"그 쪽으로는 할 수 없어."
				return;
			}

			Ballistica dash = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

			if (!dash.collisionPos.equals(target)
					|| Actor.findChar(target) != null
					|| (Dungeon.level.solid[target] && !Dungeon.level.passable[target])
					|| Dungeon.level.map[target] == Terrain.CHASM){
				Dungeon.hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_dash_bad_position"));	//"그 쪽으로는 할 수 없어."
				return;
			}

			for (int c : dash.subPath(0, Dungeon.level.distance(hero.pos, target)-1)) {
				if (Dungeon.level.map[c] != Terrain.CHASM) {
					GameScene.add( Blob.seed( c, 1, Fire.class ) );
				}
			}

			hero.busy();
			Sample.INSTANCE.play(Assets.Sounds.MISS);
			hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, target)));
			hero.sprite.jump(hero.pos, target, 0, 0.1f, new Callback() {
				@Override
				public void call() {
					if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
						Door.leave( hero.pos );
					}
					hero.pos = target;
					Dungeon.level.occupyCell(hero);
					hero.next();
				}
			});

			Item.updateQuickslot();
			AttackIndicator.updateState();

			bicycle.use(ProfessionalRideMove.SKID_MARK.chargeReq);
		}

		@Override
		public String prompt() {
			return Messages.get(SpiritBow.class, "prompt");
		}
	};

	private static CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			Bicycle bicycle = hero.belongings.getItem(Bicycle.class);
			if (bicycle == null || target == null) {
				return;
			}

			Char ch = Actor.findChar(target);
			if (ch == null || ch == hero || !Dungeon.level.adjacent(hero.pos, ch.pos)) {
				hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_"));	//"그렇게는 할 수 없어."
				return;
			}

			Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.STOP_TARGET);
			trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
			WandOfBlastWave.throwChar(ch, trajectory, 3, false, true, hero);
			hero.sprite.zap(target);
			hero.spendAndNext(Actor.TICK);
			Sample.INSTANCE.play(Assets.Sounds.BLAST);

			bicycle.use(ProfessionalRideMove.SLAM.chargeReq);
		}

		@Override
		public String prompt() {
			return Messages.get(SpiritBow.class, "prompt");
		}
	};

	public static class BicycleCooldown extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xC7C4C9); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 5); }
	}

	public static class DriftCooldown extends Buff {
		int cooldown = 0;

		public void set() {
			cooldown = 5;
		}

		public void kill() {
			cooldown --;
			if (cooldown <= 0) {
				detach();
			}
		}

		private static final String COOLDOWN = "cooldown";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(COOLDOWN, cooldown);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			cooldown = bundle.getInt(COOLDOWN);
		}
	}

	public static class DriftBuff extends FlavourBuff {
		public int icon() { return BuffIndicator.RECHARGING; }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	}

	public static class BicycleBuff extends Buff implements ActionIndicator.Action {

		private boolean isProfessional = false;

		{
			type = buffType.NEUTRAL;
			announced = false;
			if (hero.subClass == HeroSubClass.SHIROKO_EX_PROFESSIONAL_RIDE) {
				isProfessional = true;
			}
		}

		private static final String ISPROFESSIONAL = "isProfessional";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ISPROFESSIONAL, isProfessional);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			isProfessional = bundle.getBoolean(ISPROFESSIONAL);
			if (isProfessional) {
				ActionIndicator.setAction(BicycleBuff.this);
			}
		}

		@Override
		public boolean act() {
			if (hero.subClass == HeroSubClass.SHIROKO_EX_PROFESSIONAL_RIDE) {
				ActionIndicator.setAction(BicycleBuff.this);
			}
			Bicycle bicycle = Dungeon.hero.belongings.getItem(Bicycle.class);
			if (bicycle == null) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}

		@Override
		public int icon() {
			return BuffIndicator.HASTE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0xC7C4C9);
		}

		@Override
		public float iconFadePercent() {
			Bicycle bicycle = Dungeon.hero.belongings.getItem(Bicycle.class);
			if (bicycle != null) {
				return Math.max(0, bicycle.chargePercent());
			} else {
				return super.iconFadePercent();
			}
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			Bicycle bicycle = Dungeon.hero.belongings.getItem(Bicycle.class);
			if (bicycle != null) {
				String desc = Messages.get(this, "desc_1");
				if (hero.subClass != HeroSubClass.SHIROKO_EX_PROFESSIONAL_RIDE) {
					desc += "\n\n" + Messages.get(this, "desc_acc");
				}
				desc += "\n\n" + Messages.get(this, "desc_2", bicycle.maxCharge(), bicycle.charge());
				return desc;
			} else {
				return super.desc();
			}
		}

		@Override
		public void detach() {
			super.detach();
			Buff.affect(hero, BicycleCooldown.class, 5f);
			ActionIndicator.clearAction();
		}


		//액션인디케이터 내용
		@Override
		public String actionName() {
			return Messages.get(this, "action_name");
		}

		@Override
		public int actionIcon() {
			return HeroIcon.PROFESSIONAL_RIDE;
		}

		@Override
		public Visual secondaryVisual() {
			BitmapText txt = new BitmapText(PixelScene.pixelFont);
			Bicycle bicycle = Dungeon.hero.belongings.getItem(Bicycle.class);
			if (bicycle != null) {
				txt.text( Messages.decimalFormat("#.##", bicycle.charge()));
				txt.hardlight(CharSprite.POSITIVE);
				txt.measure();
				return txt;
			} else {
				return null;
			}
		}

		@Override
		public int indicatorColor() {
			return 0xC7C4C9;
		}

		@Override
		public void doAction() {
			Bicycle bicycle = Dungeon.hero.belongings.getItem(Bicycle.class);
			if (bicycle != null) {
				GameScene.show(new WndProfessionalRide(bicycle));
			}
		}
	}

	public static class AccelerationBuff extends Buff {
		@Override
		public void detach() {
			super.detach();
			Buff.prolong(hero, Haste.class, 2f);
		}
	}
}
