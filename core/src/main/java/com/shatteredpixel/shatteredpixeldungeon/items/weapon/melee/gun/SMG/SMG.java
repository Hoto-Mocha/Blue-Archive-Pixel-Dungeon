package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.SG;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
public class SMG extends Gun {

    {
        max_round = 2;
        round = max_round;
        shotPerShoot = 3;
        shootingAccuracy = 1.2f;
    }

    @Override
    public int max(int lvl) {
        int damage = super.max(lvl);
        if (isEquipped(hero) && hero.hasTalent(Talent.CQC)) {
            damage *= 1f + 0.1f*hero.pointsInTalent(Talent.CQC);
        }
        return damage;
    }

    @Override
    public int bulletMin(int lvl) {
        return tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int bulletMax(int lvl) {
        return 3 * (tier) +
                lvl * (tier) +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int shotPerShoot() { //발사 당 탄환의 수
        int amount = shotPerShoot;
        if (hero.heroClass == HeroClass.MIYAKO) amount ++;
        return amount;
    }

    @Override
    public float reloadTime() { //재장전에 소모하는 턴
        float amount = super.reloadTime();

        amount *= 1 - (0.1f*hero.pointsInTalent(Talent.SMG_FAST_RELOAD));

        return amount;
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (hero.hasTalent(Talent.SMG_MASTER) && this.tier <= 1 + 2*hero.pointsInTalent(Talent.SMG_MASTER)) {
            req--;
        }
        return req;
    }

    @Override
    public Bullet knockBullet(){
        return new SMGBullet();
    }

    public class SMGBullet extends Bullet {
        {
            image = ItemSpriteSheet.TRIPLE_BULLET;
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            float ACC = super.accuracyFactor(owner, target);
            if (hero.heroClass == HeroClass.MIYAKO) {
                ACC *= 1.2f;
            }
            return ACC;
        }
    }
}
