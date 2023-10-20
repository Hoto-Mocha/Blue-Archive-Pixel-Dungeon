package com.shatteredpixel.shatteredpixeldungeon.items.active;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
public class Claymore extends Grenade {

    {
        image = ItemSpriteSheet.CLAYMORE;

        max_amount = 1;
        amount = max_amount;

        unique = true;
        bones = false;
    }

    @Override
    public int maxAmount() { //최대 장탄수
        return max_amount;
    }

    @Override
    public Boomer knockItem(){
        return new Blaster();
    }

    public class Blaster extends Boomer {
        {
            image = ItemSpriteSheet.CLAYMORE;
        }

        @Override
        protected void activate(int cell) {
            blast(cell);
        }

        @Override
        public void cast(final Hero user, final int dst) {
            int cell = throwPos( user, dst );
            activate(cell);
        }

        private void blast(int cell) {
            int openUpMulti = 1;
            if ((Dungeon.level.map[curUser.pos] == Terrain.DOOR || Dungeon.level.map[curUser.pos] == Terrain.OPEN_DOOR) && hero.hasTalent(Talent.RABBIT_OPEN_UP)) {
                Dungeon.level.destroy(curUser.pos);
                GameScene.updateMap( curUser.pos );
                openUpMulti += hero.pointsInTalent(Talent.RABBIT_OPEN_UP);
            }
            Ballistica aim = new Ballistica(hero.pos, cell, Ballistica.WONT_STOP);

            int maxDist = 4*openUpMulti;
            int dist = Math.min(aim.dist, maxDist);

            ConeAOE cone = new ConeAOE(aim,
                    dist,
                    30*openUpMulti,
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

            for (Char ch : affected) {
                ch.damage(2*Random.NormalIntRange(5 + buffedLvl() + Dungeon.scalingDepth(), 10 + 2*buffedLvl() + Dungeon.scalingDepth()*2)*openUpMulti, curUser);
                if (Random.Float() < 0.4f + 0.1f*buffedLvl()) {
                    Buff.affect(ch, Cripple.class, 5f);
                }
            }

            hero.spendAndNext(1f);
            updateQuickslot();
            Sample.INSTANCE.play( Assets.Sounds.BLAST, 1, 1f - 0.1f*openUpMulti );
        }
    }
}
