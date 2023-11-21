package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
public class GL extends Gun {

    {
        max_round = 2;
        round = max_round;
        reload_time = 4f;
        explode = true;
    }

    @Override
    public int bulletMin(int lvl) {
        if (hero.heroClass == HeroClass.YUZU) {
            return 2*super.bulletMin(lvl);
        } else {
            return super.bulletMin(lvl);
        }
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
        if (hero.hasTalent(Talent.GL_MASTER) && this.tier <= 3) {
            req -= hero.pointsInTalent(Talent.GL_MASTER);
        }
        return req;
    }

    @Override
    public float reloadTime() {
        float amount = super.reloadTime();

        if (hero.heroClass == HeroClass.YUZU) {
            amount --;
        }

        amount *= 1 - (0.1f*hero.pointsInTalent(Talent.GL_FAST_RELOAD));

        return amount;
    }

    @Override
    public Bullet knockBullet(){
        return new GLBullet();
    }

    public class GLBullet extends Bullet {
        {
            image = ItemSpriteSheet.GRENADE_BULLET;
        }
    }
}
