package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
public class SR extends Gun {

    {
        max_round = 1;
        round = max_round;
        shootingAccuracy = 2f;
    }

    @Override
    public int bulletMin(int lvl) {
        return 2 * tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int bulletMax(int lvl) {
        return 6 * (tier+2) +
                lvl * (tier+2) +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (hero.hasTalent(Talent.SR_MASTER) && this.tier <= 1 + 2*hero.pointsInTalent(Talent.SR_MASTER)) {
            req--;
        }
        return req;
    }

    @Override
    public float reloadTime() { //재장전에 소모하는 턴
        float amount = super.reloadTime();

        amount *= 1 - (0.1f*hero.pointsInTalent(Talent.SR_FAST_RELOAD));

        return amount;
    }

    @Override
    public Bullet knockBullet(){
        return new SRBullet();
    }

    public class SRBullet extends Bullet {
        {
            image = ItemSpriteSheet.SNIPER_BULLET;
        }

        @Override
        public int damageRoll(Char owner) {
            if (owner instanceof Hero) {
                Hero hero = (Hero)owner;
                Char enemy = hero.enemy();
                if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                    //deals 25% toward max to max on surprise, instead of min to max.
                    int diff = bulletMax() - bulletMin();
                    int damage = augment.damageFactor(Random.NormalIntRange(
                            bulletMin() + Math.round(diff/(4f-hero.pointsInTalent(Talent.CLAM_BREATHING))),
                            bulletMax()));
                    int exStr = hero.STR() - STRReq();
                    if (exStr > 0) {
                        damage += Random.IntRange(0, exStr);
                    }
                    return damage;
                }
            }
            return super.damageRoll(owner);
        }
    }


}
