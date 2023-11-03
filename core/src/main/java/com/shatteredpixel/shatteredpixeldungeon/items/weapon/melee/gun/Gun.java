package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InfiniteBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShockBulletCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipingBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StickyGrenade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.APBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.ElectricBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.ElementalBulletBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.ExplosiveBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.IceBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.IncendiaryBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.YellowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.AmmoBox;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.active.IronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.GL;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Gun extends MeleeWeapon {
    public static final String AC_SHOOT		= "SHOOT";
    public static final String AC_RELOAD    = "RELOAD";
    public static final String AC_AMMO      = "AMMO";
    public static final String AC_JUMP      = "JUMP";

    protected int max_round; //최대 장탄수
    protected int round; //현재 장탄수
    protected float reload_time = 2f; //재장전 시간
    protected int shotPerShoot = 1; //발사 당 탄환의 개수
    protected float shootingSpeed = 1f; //발사 시 소모하는 턴의 배율. 낮을수록 빠르다
    protected float shootingAccuracy = 1f; //발사 시 탄환 정확성의 배율. 높을 수록 정확하다
    protected boolean explode = false; //탄환 폭발 여부
    public static final String TXT_STATUS = "%d/%d";

    public boolean doubleBarrelSpecial = false;

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;
    }

    private static final String ROUND = "round";
    private static final String MAX_ROUND = "max_round";
    private static final String RELOAD_TIME = "reload_time";
    private static final String SHOT_PER_SHOOT = "shotPerShoot";
    private static final String SHOOTING_SPEED = "shootingSpeed";
    private static final String SHOOTING_ACCURACY = "shootingAccuracy";
    private static final String EXPLODE = "explode";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAX_ROUND, max_round);
        bundle.put(ROUND, round);
        bundle.put(RELOAD_TIME, reload_time);
        bundle.put(SHOT_PER_SHOOT, shotPerShoot);
        bundle.put(SHOOTING_SPEED, shootingSpeed);
        bundle.put(SHOOTING_ACCURACY, shootingAccuracy);
        bundle.put(EXPLODE, explode);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        max_round = bundle.getInt(MAX_ROUND);
        round = bundle.getInt(ROUND);
        reload_time = bundle.getFloat(RELOAD_TIME);
        shotPerShoot = bundle.getInt(SHOT_PER_SHOOT);
        shootingSpeed = bundle.getFloat(SHOOTING_SPEED);
        shootingAccuracy = bundle.getFloat(SHOOTING_ACCURACY);
        explode = bundle.getBoolean(EXPLODE);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            actions.add(AC_SHOOT);
            actions.add(AC_RELOAD);
            if (hero.hasTalent(Talent.AMMO_BOX)) {
                actions.add(AC_AMMO);
            }
            if (this instanceof GL && hero.hasTalent(Talent.ROCKET_JUMP)) {
                actions.add(AC_JUMP);
            }
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {
            if (!isEquipped( hero )) {
                usesTargeting = false;
                GLog.w(Messages.get(this, "not_equipped"));
            } else {
                if (round <= 0) { //현재 탄창이 0이면 AC_RELOAD 버튼을 눌렀을 때처럼 작동
                    execute(hero, AC_RELOAD);
                } else {
                    usesTargeting = true;
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                }
            }
        }
        if (action.equals(AC_RELOAD)) {
            if (isAllLoaded()){
                GLog.w(Messages.get(this, "already_loaded"));
            } else {
                reload();
            }
        }
        if (action.equals(AC_AMMO)) {
            int cell = Dungeon.level.map[hero.pos];
            if (cell == Terrain.CHASM || cell == Terrain.DOOR || cell == Terrain.OPEN_DOOR) {
                hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_cannot_ammo_place"));     //"이 곳엔 설치할 수 없어요..."
            } else {
                if (hero.buff(AmmoBox.AmmoBoxCooldown.class) != null) {
                    hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_ammo_place_cooldown"));     //"아직 사용할 수 없어요..."
                } else {
                    hero.sprite.operate(hero.pos);
                    Dungeon.level.drop(new AmmoBox(1+2*hero.pointsInTalent(Talent.AMMO_BOX)), hero.pos);
                    hero.spendAndNext(Actor.TICK);
                    hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_ammo_place"));    //"탄약 상자를 설치하겠습니다...!"
                }
            }
        }
        if (action.equals(AC_JUMP)) {
            if (isAllLoaded()) {
                usesTargeting = false;
                curUser = hero;
                curItem = this;
                GameScene.selectCell(jumper);
            } else {
                hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_ammo_lack"));    //장탄수가 부족해요..."
            }
        }
    }

    public boolean isAllLoaded() {
        return round >= maxRound();
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Dagger.sneakAbility(hero, 6, 4, this);
    }

    public void reload() {
        quickReload();
        hero.busy();
        hero.sprite.operate(hero.pos);

        if (hero.belongings.secondWep != null) {
            if (hero.belongings.secondWep instanceof Gun) {
                Gun secondGun = ((Gun) hero.belongings.secondWep);
                if (hero.hasTalent(Talent.DOUBLE_BARREL_2) && secondGun.round < secondGun.maxRound()) {
                    secondGun.quickReload();
                    hero.spendAndNext(Math.max(3-hero.pointsInTalent(Talent.DOUBLE_BARREL_2), 0));
                }
            }
        }

        onReload();
        Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
        hero.spendAndNext(reloadTime());
        GLog.i(Messages.get(this, "reload"));
    }

    public void onReload() {
        if (hero.hasTalent(Talent.RELOADING_SHIELD)) {
            Buff.affect(hero, Barrier.class).setShield(1+2*hero.pointsInTalent(Talent.RELOADING_SHIELD));
        }
        if (hero.hasTalent(Talent.TACTICAL_SHIELD_3) && hero.buff(IronHorus.TacticalShieldBuff.class) != null && hero.buff(Talent.TacticalInvisibilityTracker.class) == null) {
            Buff.affect(hero, Invisibility.class, 3f*hero.pointsInTalent(Talent.TACTICAL_SHIELD_3));
            Buff.affect(hero, Talent.TacticalInvisibilityTracker.class);
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof ElementalBulletBuff) {
                buff.detach();
            }
        }
        if (hero.subClass == HeroSubClass.SHIROKO_EX_ELEMENTAL_BULLET) {
            Buff.affect(hero, ElementalBullet.class).set(reloadTime()+1);
        }
        if (hero.subClass == HeroSubClass.NOA_EX_LARGE_MAGAZINE && hero.buff(InfiniteBullet.infiniteBulletCooldown.class) == null) {
            Buff.affect(hero, InfiniteBullet.class).set(reloadTime());
            Buff.affect(hero, InfiniteBullet.infiniteBulletCooldown.class).set();
        }
        if (hero.hasTalent(Talent.LARGE_MAGAZINE_3) && hero.buff(Talent.BulletTimeCooldown.class) == null) {
            for (Char ch : Actor.chars()) {
                if (Dungeon.level.heroFOV[ch.pos] && ch.alignment == Char.Alignment.ENEMY) {
                    Buff.affect(ch, Slow.class, (reloadTime()+3*hero.pointsInTalent(Talent.LARGE_MAGAZINE_3)-1));
                }
            }
            Buff.affect(hero, Talent.BulletTimeCooldown.class, 30f);
        }
        if (hero.subClass == HeroSubClass.MIYU_EX_SNIPING_BULLET) {
            Buff.affect(hero, SnipingBullet.class);
        }
        if (hero.buff(Talent.FastHandTracker.class) != null) hero.buff(Talent.FastHandTracker.class).detach();
        if (hero.buff(Talent.PerfectHandTracker.class) != null) hero.buff(Talent.PerfectHandTracker.class).detach();
        if (hero.hasTalent(Talent.GAME_START_3)) {
            int area[];
            switch (hero.pointsInTalent(Talent.GAME_START_3)) {
                case 1: default:
                    area = PathFinder.NEIGHBOURS9;
                    break;
                case 2:
                    area = PathFinder.NEIGHBOURS25;
                    break;
                case 3:
                    area = PathFinder.NEIGHBOURS49;
                    break;
            }
            for (int i : area) {
                int cell = hero.pos + i;
                int terrain = Dungeon.level.map[cell];
                if (terrain == Terrain.GRASS || terrain == Terrain.EMBERS) {
                    int toTerrain = Terrain.FURROWED_GRASS;
                    if (Random.Float() < 0.25f && Regeneration.regenOn()) {
                        toTerrain = Terrain.HIGH_GRASS;
                    }
                    Level.set(cell, toTerrain);
                    GameScene.updateMap( cell );
                }
            }
        }
    }

    public void quickReload() {
        round = maxRound();
        updateQuickslot();
    }

    public void manualReload() {
        manualReload(1, false);
    }

    public void manualReload(int amount, boolean overReload) {
        round += amount;
        if (overReload) {
            if (round > maxRound() + amount) { //최대 장탄수를 넘을 수는 있지만, 중첩은 불가
                round = maxRound() + amount;
            }
        } else {
            if (round > maxRound()) {
                round = maxRound();
            }
        }

        updateQuickslot();
    }

    public int shotPerShoot() { //발사 당 탄환의 수
        return shotPerShoot;
    }

    public int maxRound() { //최대 장탄수
        int amount = max_round;

        amount += Dungeon.hero.pointsInTalent(Talent.LARGE_MAGAZINE);

        return amount;
    }

    public int round() {
        return round;
    }

    public float reloadTime() { //재장전에 소모하는 턴
        if (hero.buff(IronHorus.TacticalShieldBuff.class) != null && hero.subClass == HeroSubClass.HOSHINO_EX_TACTICAL_SHIELD) {
            return 0;
        }

        float amount = reload_time;

        amount += hero.pointsInTalent(Talent.LARGE_MAGAZINE);

        if (hero.buff(Talent.FastHandTracker.class) != null) {
            amount -= hero.pointsInTalent(Talent.FAST_HAND);
        }

        if (hero.hasTalent(Talent.LARGE_MAGAZINE_1)) {
            ArrayList<Char> chars = new ArrayList<>();
            for (Char ch : Actor.chars()) {
                if (Dungeon.level.heroFOV[ch.pos] && ch.alignment == Char.Alignment.ENEMY) {
                    chars.add(ch);
                }
            }
            if (chars.size() > 0) {
                amount *= Math.pow(1/(1+0.05*hero.pointsInTalent(Talent.LARGE_MAGAZINE_1)), chars.size());  //1.05/1.1/1.15로 시야 내의 적의 수만큼 나눔
            }
        }

        if (hero.buff(Talent.PerfectHandTracker.class) != null) {
            amount *= Math.pow(1-0.2f*hero.pointsInTalent(Talent.PENETRATION_SHOT_3), hero.buff(Talent.PerfectHandTracker.class).getKilled());
        }

        return amount;
    }

    @Override
    public int max(int lvl) {
        return  3*(tier+1) +
                lvl*(tier+1); //근접 무기로서의 최대 데미지
    }

    public int bulletMin(int lvl) {
        return tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero);
    }
    public int bulletMin() {
        return bulletMin(this.buffedLvl());
    }

    //need to be overridden
    public int bulletMax(int lvl) {
        return 0; //총알의 최대 데미지
    }
    public int bulletMax() {
        return bulletMax(this.buffedLvl());
    }

    public int bulletDamage() {
        int damage = Random.NormalIntRange(bulletMin(), bulletMax());
        if (doubleBarrelSpecial) {
            damage = Math.round(damage * 0.667f);

        }

        damage = augment.damageFactor(damage);  //증강에 따라 변화하는 효과
        return damage;
    }

    @Override
    protected float baseDelay(Char owner) {
        if (doubleBarrelSpecial){
            return 0f;
        } else{
            return super.baseDelay(owner);
        }
    }


    @Override
    public String info() {
        String info = super.info();
        //근접 무기의 설명을 모두 가져옴, 여기에서 할 것은 근접 무기의 설명에 추가로 생기는 문장을 더하는 것
        if (levelKnown) { //감정되어 있을 때
            info += "\n\n" + Messages.get(Gun.class, "gun_desc",
                    augment.damageFactor(bulletMin(buffedLvl())), augment.damageFactor(bulletMax(buffedLvl())), shotPerShoot(), round, maxRound(), new DecimalFormat("#.##").format(reloadTime()));
        } else { //감정되어 있지 않을 때
            info += "\n\n" + Messages.get(Gun.class, "gun_typical_desc",
                    augment.damageFactor(bulletMin(0)), augment.damageFactor(bulletMax(0)), shotPerShoot(), round, maxRound(), new DecimalFormat("#.##").format(reloadTime()));
        }
        //DecimalFormat("#.##")은 .format()에 들어가는 매개변수(실수)를 "#.##"형식으로 표시하는데 사용된다.
        //가령 5.55555가 .format()안에 들어가서 .format(5.55555)라면, new DecimalFormat("#.##").format(5.55555)는 5.55라는 String 타입의 값을 반환한다.

        return info;
    }

    @Override
    public String status() { //아이템 칸 오른쪽 위에 나타내는 글자
        return Messages.format(TXT_STATUS, round, maxRound()); //TXT_STATUS 형식(%d/%d)으로, round, maxRound() 변수를 순서대로 %d부분에 출력
    }

    @Override
    public int targetingPos(Hero user, int dst) {
        return knockBullet().targetingPos(user, dst);
    }

    //needs to be overridden
    public Bullet knockBullet(){
        return new Bullet();
    }

    public class Bullet extends MissileWeapon {

        public boolean isDoubleBarrel = false;

        {
            hitSound = Assets.Sounds.PUFF;
            tier = Gun.this.tier;
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            boolean isDebuffed = false;
            for (Buff buff : defender.buffs()) {
                if (buff.type == Buff.buffType.NEGATIVE) {
                    isDebuffed = true;
                    break;
                }
            }
            if (isDebuffed && hero.hasTalent(Talent.ELEMENTAL_BULLET_3)) {
                damage = Math.round(damage*(1 + 0.2f * hero.pointsInTalent(Talent.ELEMENTAL_BULLET_3)/3f)); //+6.67%/+13.33%/+20%
            }
            if (hero.subClass == HeroSubClass.HOSHINO_EX_TACTICAL_SHIELD && hero.buff(IronHorus.TacticalShieldBuff.class) != null) {
                Buff.affect(hero, Barrier.class).set(1, 10+5*hero.pointsInTalent(Talent.TACTICAL_SHIELD_2));
                BuffIndicator.refreshHero();
            }
            if (hero.heroClass == HeroClass.MIYAKO && Random.Float() < 0.1f) {
                Buff.affect(defender, Vulnerable.class, 3f);
            }
            if (hero.buff(APBullet.class) != null) {
                damage *= 1-0.2f+0.05f*hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1);
            }
            if (hero.buff(IncendiaryBullet.class) != null ) {
                if (Random.Float() < 0.2f+0.1f*hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1)) {
                    Buff.affect(defender, Burning.class).reignite(defender);
                }
            }
            if (hero.buff(IceBullet.class) != null ) {
                Buff.affect(defender, Chill.class, 2+hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1));
                if (defender.buff(Chill.class) != null) {
                    if (Dungeon.level.map[defender.pos] == Terrain.WATER && defender.buff(Chill.class).cooldown() > 20-3*hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1)) {
                        new FlavourBuff() {
                            {
                                actPriority = VFX_PRIO;
                            }

                            public boolean act() {
                                Buff.affect(target, Frost.class, Frost.DURATION);
                                return super.act();
                            }
                        }.attachTo(defender);
                    }
                }
            }
            if (hero.buff(ElectricBullet.class) != null) {
                ArrayList<Char> affected = new ArrayList<>();
                ArrayList<Lightning.Arc> arcs = new ArrayList<>();
                Shocking.arc( attacker, defender, 2, affected, arcs );
                affected.remove(defender);  //직접 명중한 대상은 제외
                for (Char ch : affected) {
                    ch.damage( Math.round( damage * (0.2f+0.1f*hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1)) ), Shocking.class );
                }
            }
            if (hero.heroClass == HeroClass.SHIROKO) {
                int amount = 0;
                float chance = 1/(float)((Gun) hero.belongings.weapon).shotPerShoot();
                amount ++;
                if (hero.buff(ExplosiveBullet.class) != null) {
                    amount += 2+hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1);
                }
                if (hero.hasTalent(Talent.RAPID_SHOOTING)) {
                    if (Random.Float() < 0.5f*hero.pointsInTalent(Talent.RAPID_SHOOTING)) {
                        amount ++;
                    }
                }
                for (int i=0; i<amount; i++) {
                    if (Random.Float() < chance) {
                        int explodeDamage = Random.NormalIntRange(Dungeon.scalingDepth()/5, Dungeon.scalingDepth()/2+3);
                        explodeDamage += hero.pointsInTalent(Talent.ENHANCED_EXPLODE);
                        explodeDamage -= defender.drRoll();
                        CellEmitter.center(defender.pos).burst(BlastParticle.FACTORY, 1);
                        defender.damage(explodeDamage, hero);
                    }
                }
            }
            if (Gun.this.isAllLoaded() && hero.hasTalent(Talent.DOUBLE_TAP)) {
                new FlavourBuff() {
                    {
                        actPriority = VFX_PRIO;
                    }

                    public boolean act() {
                        Buff.affect(target, Talent.DoubleTapTracker.class);
                        return super.act();
                    }
                }.attachTo(hero);
            }
            if (hero.hasTalent(Talent.BEST_SNIPER)) {
                if (damage >= defender.HT*(1-(1/(6f-hero.pointsInTalent(Talent.BEST_SNIPER)))) && damage <= defender.HT) {
                    damage = defender.HT;
                    defender.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
                }
            }
            if (hero.buff(SnipingBullet.class) != null) {
                int distance = Dungeon.level.distance(attacker.pos, defender.pos) - 1;
                float multi = ((Math.min(distance, 12)+4)/(8f-hero.pointsInTalent(Talent.SNIPING_BULLET_1)));
                damage *= multi;
                if (hero.hasTalent(Talent.SNIPING_BULLET_3) && (distance >= (12-2*hero.pointsInTalent(Talent.SNIPING_BULLET_3)))) {
                    Buff.affect(defender, Paralysis.class, 3f);
                    Buff.affect(attacker, ShockBulletCooldown.class).set();
                }
                hero.buff(SnipingBullet.class).detach();
            }
            return Gun.this.proc(attacker, defender, damage);
        }

        @Override
        public int buffedLvl(){
            return Gun.this.buffedLvl();
        }

        @Override
        public int damageRoll(Char owner) {
            int damage = bulletDamage();
            return damage;
        }

        @Override
        public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
            return Gun.this.hasEnchant(type, owner);
        }

        @Override
        public float delayFactor(Char user) {
            return Gun.this.delayFactor(user) * shootingSpeed;
        }
        
        @Override
        public float accuracyFactor(Char owner, Char target) {
            float ACC = super.accuracyFactor(owner, target);
            if (owner instanceof Hero) {
                ACC *= shootingAccuracy;
            }
            return ACC;
        }

        @Override
        public int STRReq(int lvl) {
            return Gun.this.STRReq();
        }

        @Override
        protected void onThrow( int cell ) {
            if (explode || hero.subClass == HeroSubClass.YUZU_EX_GAME_START) {
                Char chInPos = Actor.findChar(cell);
                if (hero.subClass == HeroSubClass.YUZU_EX_STICKY_GRENADE && chInPos != null) {
                    Buff.affect(chInPos, StickyGrenade.class).attach(this, curUser);
                    chInPos.sprite.flash();
                    Sample.INSTANCE.play(Assets.Sounds.HIT);
                    if (chInPos instanceof Mob) ((Mob)chInPos).beckon(curUser.pos);
                } else {
                    ArrayList<Char> targets = new ArrayList<>();
                    int shootArea[] = PathFinder.NEIGHBOURS9;
                    if (explode && hero.subClass == HeroSubClass.YUZU_EX_GAME_START) {
                        shootArea = PathFinder.NEIGHBOURS21;
                    }

                    for (int i : shootArea){
                        int c = cell + i;
                        if (c >= 0 && c < Dungeon.level.length()) {
                            if (Dungeon.level.heroFOV[c]) {
                                CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 4);
                            }
                            if (Dungeon.level.flamable[c]) {
                                Dungeon.level.destroy(c);
                                GameScene.updateMap(c);
                            }
                            Char ch = Actor.findChar(c);
                            if (ch != null && !targets.contains(ch)) {
                                targets.add(ch);
                            }
                        }
                    }

                    ArrayList<Char> additionalTargets = new ArrayList<>();
                    if (hero.hasTalent(Talent.GAME_START_2)) {
                        for (Char target : targets) {
                            for (int i : PathFinder.NEIGHBOURS9) {
                                int c = target.pos + i;
                                if (c >= 0 && c < Dungeon.level.length()) {
                                    if (Dungeon.level.heroFOV[c]) {
                                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 4);
                                    }
                                    if (Dungeon.level.flamable[c]) {
                                        Dungeon.level.destroy(c);
                                        GameScene.updateMap(c);
                                    }
                                    Char ch = Actor.findChar(c);
                                    if (ch != null && !additionalTargets.contains(ch) && !targets.contains(ch)) {   //기존 폭발에 데미지를 입을 대상은 제외함
                                        additionalTargets.add(ch);
                                    }
                                }
                            }
                        }
                    }

                    for (Char target : targets){
                        if (target == hero && hero.heroClass == HeroClass.YUZU) {
                            curUser.shoot(target, this, 0.25f, 0, 1);
                        } else {
                            curUser.shoot(target, this, 1f, 0, 1f, hero.heroClass == HeroClass.YUZU);
                        }
                        if (target == hero && !target.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "ondeath"));
                        }
                    }

                    for (Char target : additionalTargets){
                        if (target == hero && hero.heroClass == HeroClass.YUZU) {
                            curUser.shoot(target, this, hero.pointsInTalent(Talent.GAME_START_2)/3f*0.25f, 0, 1);
                        } else {
                            curUser.shoot(target, this, hero.pointsInTalent(Talent.GAME_START_2)/3f, 0, 1, hero.heroClass == HeroClass.YUZU);
                        }
                        if (target == hero && !target.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "ondeath"));
                        }
                    }

                    CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    Sample.INSTANCE.play( Assets.Sounds.BLAST );
                }
            } else {
                Char enemy = Actor.findChar( cell );
                for (int i = 0; i < shotPerShoot(); i++) { //데미지 입히는 것과 발사 시 주변에서 나는 연기를 shotPerShoot만큼 반복
                    if (enemy == null || enemy == curUser) {
                        parent = null;
                        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                    } else {
                        if (!curUser.shoot( enemy, this, 1f, 0, 1f, hero.heroClass == HeroClass.YUZU )) {
                            CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                        }
                    }
                }
            }

            round --;
            if (hero.buff(InfiniteBullet.class) != null) {
                round ++;
            }
            if (round == 0 && hero.hasTalent(Talent.LAST_BULLET)) {
                Buff.affect(hero, Barrier.class).setShield(2+3*hero.pointsInTalent(Talent.LAST_BULLET));
            }
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) { //주변의 적들을 영웅의 위치로 모이게 하는 구문
                if (mob.paralysed <= 0
                        && Dungeon.level.distance(curUser.pos, mob.pos) <= 4
                        && mob.state != mob.HUNTING) {
                    mob.beckon( curUser.pos );
                }
            }
            updateQuickslot();
        }

        @Override
        public void throwSound() {
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }

        @Override
        public void cast(final Hero user, final int dst) {
            cast(user, dst, false);
        }

        public void cast(final Hero user, final int dst, boolean doubleBarrel) {
            isDoubleBarrel = doubleBarrel;
            super.cast(user, dst);
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    execute(hero, AC_RELOAD);
                } else {
                    //미유 EX 관통 사격 능력
                    if (hero.subClass == HeroSubClass.MIYU_EX_PENETRATION_SHOT) {

                        Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                        ArrayList<Char> chars = new ArrayList<>();

                        for (int c : beam.subPath(1, beam.dist)) {
                            Char ch;

                            if ((ch = Actor.findChar( c )) != null) {
                                chars.add(ch);
                            }

                            CellEmitter.center( c ).burst( YellowParticle.BURST, Random.IntRange( 1, 2 ) );
                        }
                        int prevRound = round;
                        int damageBonus = Math.min(chars.size() * hero.pointsInTalent(Talent.PENETRATION_SHOT_1), 15);
                        int killed = 0;
                        for (Char ch : chars) {
                            Bullet bullet = knockBullet();
                            bullet.proc(curUser, ch, bullet.damageRoll(curUser) + damageBonus);
                            bullet.onThrow(ch.pos);
                            if (!ch.isAlive()) killed++;
                        }
                        if (Gun.this instanceof GL && chars.isEmpty()) {
                            Bullet bullet = knockBullet();
                            bullet.onThrow(beam.collisionPos);
                        }
                        if (hero.hasTalent(Talent.PENETRATION_SHOT_3) && killed > 0 && hero.buff(Talent.PerfectHandTracker.class) == null) {
                            Buff.affect(curUser, Talent.PerfectHandTracker.class).set(killed);
                        }
                        round = prevRound - 1;
                        if (hero.buff(InfiniteBullet.class) != null) {
                            round = prevRound;
                        }
                        updateQuickslot();

                        Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
                        CellEmitter.get(curUser.pos).burst(SmokeParticle.FACTORY, 2);
                        CellEmitter.center(curUser.pos).burst(BlastParticle.FACTORY, 2);

                        curUser.sprite.zap(target);
                        hero.spendAndNext(hero.attackDelay());

                        int cell = beam.path.get(Math.min(beam.dist, target));
                        curUser.sprite.parent.add(new Beam.ShotRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));

                        if (hero.hasTalent(Talent.PENETRATION_SHOT_2) && hero.buff(Talent.SuppressingFireCooldown.class) == null) {
                            boolean affected = false;
                            PathFinder.buildDistanceMap( curUser.pos, BArray.not( Dungeon.level.solid, null ), hero.pointsInTalent(Talent.PENETRATION_SHOT_2) );
                            ArrayList<Char> chToTerror = new ArrayList<>();
                            for (int i = 0; i < PathFinder.distance.length; i++) {
                                if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                                    Char ch = Actor.findChar(i);
                                    if (ch != null) {
                                        if (ch instanceof Mob) {
                                            chToTerror.add(ch);
                                        }
                                    }
                                }
                            }
                            for (Char ch : chToTerror) {
                                Buff.affect(ch, Terror.class, 3);
                                if (!affected) {
                                    affected = true;
                                }
                            }
                            if (affected) {
                                Buff.affect(curUser, Talent.SuppressingFireCooldown.class, 20f);
                            }
                        }
                    } else {
                        knockBullet().cast(curUser, target);
                    }
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    private CellSelector.Listener jumper = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_cannot_jump"));   //"그런 곳으로... 점프를 하라고요...?"
                } else {
                    if (hero.rooted) {
                        hero.yellW(Messages.get(Hero.class, hero.heroClass.name() + "_cannot_jump_rooted"));    //"발이 묶여버렸어요..."
                        PixelScene.shake( 1, 1f );
                        return;
                    }

                    Ballistica route = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    int cell = route.collisionPos;

                    //can't occupy the same cell as another char, so move back one.
                    int backTrace = route.dist-1;
                    while (Actor.findChar( cell ) != null && cell != hero.pos) {
                        cell = route.path.get(backTrace);
                        backTrace--;
                    }

                    final int dest = cell;
                    hero.busy();
                    hero.sprite.jump(hero.pos, cell, new Callback() {
                        @Override
                        public void call() {
                            hero.move(dest);
                            Dungeon.level.occupyCell(hero);
                            Dungeon.observe();
                            GameScene.updateFog();

                            Invisibility.dispel();
                            hero.spendAndNext(Actor.TICK);
                        }
                    });

                    int damage = 40-10*hero.pointsInTalent(Talent.ROCKET_JUMP);
                    damage -= hero.drRoll();
                    CellEmitter.center(hero.pos).burst(BlastParticle.FACTORY, 1);
                    CellEmitter.get(hero.pos).burst(SmokeParticle.FACTORY, 3);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
                    if (damage >= hero.HP) damage = hero.HP-1;
                    hero.damage(damage, this);

                    round = 0;
                    updateQuickslot();
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(Hero.class, "rocketjump");
        }
    };
}
