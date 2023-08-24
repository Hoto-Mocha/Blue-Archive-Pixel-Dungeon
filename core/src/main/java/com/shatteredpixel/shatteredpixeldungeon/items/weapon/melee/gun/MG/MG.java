package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.HG;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
public class MG extends Gun {

    private boolean riot = false;
    private boolean shootAll = false;

    {
        max_round = 4;
        round = max_round;
        reload_time = 3f;
        shotPerShoot = 3;
        shootingAccuracy = 0.7f;
    }

    private static final String RIOT = "riot";
    private static final String SHOOTALL = "shootAll";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RIOT, riot);
        bundle.put(SHOOTALL, shootAll);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        riot = bundle.getBoolean(RIOT);
        shootAll = bundle.getBoolean(SHOOTALL);
    }

    public static final String AC_RIOT	 = "RIOT";
    public static final String AC_SHOOTALL = "SHOOTALL";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped( hero )) {
            if (hero.subClass == HeroSubClass.NONOMI_EX_RIOT) {
                actions.add(AC_RIOT);
            }
            if (hero.subClass == HeroSubClass.NONOMI_EX_SHOOTALL) {
                actions.add(AC_SHOOTALL);
            }
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_RIOT)) {
            riot = !riot;
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
            if (riot) {
                hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_riot_on"));
            } else {
                hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_riot_off"));
            }
        }
        if (action.equals(AC_SHOOTALL)) {
            shootAll = !shootAll;
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
            if (shootAll) {
                hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_shootall_on"));
            } else {
                hero.yellI(Messages.get(Hero.class, hero.heroClass.name() + "_shootall_off"));
            }
        }
    }

    @Override
    public int max(int lvl) {
        int damage = super.max(lvl);
        if (isEquipped(hero) && round == 0 && hero.hasTalent(Talent.SHOOTALL_3)) {
            damage *= 1f + 0.2f/3f*hero.pointsInTalent(Talent.SHOOTALL_3);
        }
        return damage;
    }

    @Override
    public int bulletMin(int lvl) {
        return Math.round((tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero))/3f);
    }

    @Override
    public int bulletMax(int lvl) {
        return Math.round((4 * (tier+1) +
                lvl * (tier+1) +
                RingOfSharpshooting.levelDamageBonus(hero))/3f);
    }

    @Override
    public float reloadTime() { //재장전에 소모하는 턴
        float amount = super.reloadTime();
        if (hero.heroClass == HeroClass.NONOMI) {
            amount -= 1f;
        }
        amount *= 1 - (0.1f*hero.pointsInTalent(Talent.MG_FAST_RELOAD));

        return amount;
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (this.tier <= 1 + 2*hero.pointsInTalent(Talent.MG_MASTER)) {
            req--;
        }
        return req;
    }

    @Override
    public Bullet knockBullet(){
        return new MGBullet();
    }

    public class MGBullet extends Bullet {
        {
            image = ItemSpriteSheet.TRIPLE_BULLET;
        }

        @Override
        protected void onThrow( int cell ) {
            if (shootAll) {
                shotPerShoot = (round + hero.pointsInTalent(Talent.SHOOTALL_1)) * 3;
                Char ch = Actor.findChar(cell);

                super.onThrow(cell);
                if (ch != null && !ch.isAlive() && Random.Float() < 0.4f/3f*hero.pointsInTalent(Talent.SHOOTALL_2)) {
                    round = maxRound();
                } else {
                    round = 0;
                }
                shotPerShoot = 3;
            } else {
                super.onThrow(cell);
            }
        }

        @Override
        public void cast(final Hero user, final int dst) {
            if (riot) {
                int cell = throwPos( user, dst );
                riot(cell);
            } else {
                super.cast(user, dst);
            }
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            float ACC = super.accuracyFactor(owner, target);
            if (hero.heroClass == HeroClass.NONOMI) {
                ACC *= 1.2f;
            }
            if (shootAll) {
                ACC *= 0.5f;
            }
            return ACC;
        }

        private void riot(int cell) {
            Ballistica aim = new Ballistica(hero.pos, cell, Ballistica.WONT_STOP);

            int maxDist = 4+2*hero.pointsInTalent(Talent.RIOT_2);
            int dist = Math.min(aim.dist, maxDist);

            ConeAOE cone = new ConeAOE(aim,
                    dist,
                    90-15*hero.pointsInTalent(Talent.RIOT_2),
                    Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

            //cast to cells at the tip, rather than all cells, better performance.
            for (Ballistica ray : cone.outerRays){
                ((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
                        MagicMissile.NOTHING_CONE,
                        hero.sprite,
                        ray.path.get(ray.dist),
                        null
                );
            }

            Invisibility.dispel();
            hero.sprite.zap(cell);

            ArrayList<Char> affected = new ArrayList<>();
            for (int c : cone.cells){
                CellEmitter.get(c).burst(SmokeParticle.FACTORY, 2);
                CellEmitter.center(c).burst(BlastParticle.FACTORY, 2);
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != hero.alignment){
                    affected.add(ch);
                }
            }
            float multi = 1f;
            multi += 0.1f * hero.pointsInTalent(Talent.RIOT_1) * affected.size();
            multi = Math.min(multi, 2.5f);


            for (Char ch : affected) {
                curUser.shoot(ch, MGBullet.this, multi, 0, multi);

                if (hero.hasTalent(Talent.RIOT_3)
                        && Random.Float() < (hero.pointsInTalent(Talent.RIOT_3) * (5-Dungeon.level.distance(hero.pos, ch.pos)) * 5) / 100f) { //(5-거리)*5*n%
                    Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.STOP_TARGET);
                    trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
                    WandOfBlastWave.throwChar(ch,
                            trajectory,
                            1+dist-Dungeon.level.distance(hero.pos, ch.pos), //현재 위치로부터 발사 거리까지의 남은 타일 수
                            false,
                            true,
                            MGBullet.this);
                }
            }

            hero.spendAndNext(delayFactor(hero));
            updateQuickslot();
            round --;
            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, Random.Float(0.33f, 0.66f) );
        }
    }
}
