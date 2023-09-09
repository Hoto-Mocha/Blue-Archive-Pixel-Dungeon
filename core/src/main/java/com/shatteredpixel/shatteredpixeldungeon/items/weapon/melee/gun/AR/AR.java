package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
public class AR extends Gun {

    {
        max_round = 4;
        round = max_round;
    }

    public int maxRound() { //최대 장탄수
        int amount = super.maxRound();

        if (hero.hasTalent(Talent.ELEMENTAL_BULLET_2)) {
            amount ++;
        }

        return amount;
    }

    @Override
    public int bulletMax(int lvl) {
        return 4 * (tier+1) +
                lvl * (tier+1) +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int STRReq(int lvl) {
        int req = super.STRReq(lvl);
        if (hero.hasTalent(Talent.AR_MASTER) && this.tier <= 1 + 2*hero.pointsInTalent(Talent.AR_MASTER)) {
            req--;
        }
        return req;
    }

    @Override
    public float reloadTime() { //재장전에 소모하는 턴
        float amount = super.reloadTime();

        if (hero.pointsInTalent(Talent.ELEMENTAL_BULLET_2) > 1) {
            amount --;
        }

        amount *= 1 - (0.1f*hero.pointsInTalent(Talent.AR_FAST_RELOAD));

        return amount;
    }

    @Override
    public Bullet knockBullet(){
        return new ARBullet();
    }

    public class ARBullet extends Bullet {
        {
            image = ItemSpriteSheet.SINGLE_BULLET;
        }

        @Override
        public float accuracyFactor(Char owner, Char target) {
            float ACC = super.accuracyFactor(owner, target);
            if (hero.heroClass == HeroClass.SHIROKO) {
                ACC *= 1.2f;
            }
            return ACC;
        }

        @Override
        protected void onThrow( int cell ) {
            super.onThrow(cell);
            if (hero.heroClass == HeroClass.SHIROKO && Random.Float() < 0.3f) {
                round ++;
            }
        }
    }
}
