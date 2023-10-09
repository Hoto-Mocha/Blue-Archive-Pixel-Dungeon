package com.shatteredpixel.shatteredpixeldungeon.items.active;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RegrowGrenade extends Grenade {

    {
        image = ItemSpriteSheet.REGROW_GRENADE;

        max_amount = 2;
        amount = max_amount;

        unique = true;
        bones = false;
    }

    @Override
    public Boomer knockItem(){
        return new Planter();
    }

    public class Planter extends Boomer {
        {
            image = ItemSpriteSheet.REGROW_GRENADE;
        }

        @Override
        protected void activate(int cell) {
            Sample.INSTANCE.play( Assets.Sounds.BLAST );

            float plants = Random.NormalIntRange(1, 3+buffedLvl());

            if (plantGrass(cell)){
                plants--;
                if (plants <= 0){
                    return;
                }
            }

            ArrayList<Integer> positions = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8){
                positions.add(cell + i);
            }
            Random.shuffle( positions );

            for (int i : positions){
                if (plantGrass(i)){
                    plants--;

                    if (plants <= 0) {
                        return;
                    }
                }
            }
        }

        private boolean plantGrass(int cell){
            int t = Dungeon.level.map[cell];
            if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
                    || t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
                    && Dungeon.level.plants.get(cell) == null){
                Level.set(cell, Terrain.HIGH_GRASS);
                GameScene.updateMap(cell);
                CellEmitter.get( cell ).burst( LeafParticle.LEVEL_SPECIFIC, 4 );
                return true;
            }
            return false;
        }
    }
}


