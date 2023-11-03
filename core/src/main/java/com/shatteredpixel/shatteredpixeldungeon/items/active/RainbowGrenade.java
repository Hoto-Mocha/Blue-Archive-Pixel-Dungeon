package com.shatteredpixel.shatteredpixeldungeon.items.active;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class RainbowGrenade extends Grenade {

    {
        image = ItemSpriteSheet.RAINBOW_GRENADE;

        max_amount = 2;
        amount = max_amount;
        dropChance = 0.20f;

        unique = true;
        bones = false;
    }

    @Override
    public int maxAmount() { //최대 장탄수
        int max = max_amount;

        max += this.buffedLvl();

        return max;
    }

    @Override
    public int explodeMinDmg(){
        return 0;
    }

    @Override
    public int explodeMaxDmg(){
        return 0;
    }

    @Override
    public Boomer knockItem(){
        return new Charmer();
    }

    public class Charmer extends Boomer {
        {
            image = ItemSpriteSheet.HP_GRENADE;
        }

        @Override
        protected void activate(int cell) {

            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }

            for (int n : PathFinder.NEIGHBOURS9) {
                int c = cell + n;
                if (c >= 0 && c < Dungeon.level.length()) {     //맵 밖은 해당하지 않음
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(RainbowParticle.FACTORY, 10);
                    }
                    Char ch = Actor.findChar(c);
                    if (ch instanceof Mob) {
                        Buff.affect(ch, Charm.class, 5+2*RainbowGrenade.this.buffedLvl()).object = curUser.id();
                    }
                }
            }

            Sample.INSTANCE.play(Assets.Sounds.BLAST);
        }
    }
}


