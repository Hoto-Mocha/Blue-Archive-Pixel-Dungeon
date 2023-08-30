package com.shatteredpixel.shatteredpixeldungeon.items.active;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlashGrenade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.SG;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class BlastGrenade extends Grenade {

    {
        image = ItemSpriteSheet.BLAST_GRENADE;

        max_amount = 1;
        amount = max_amount;
    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/10;
        return level;
    }

    @Override
    public int maxAmount() { //최대 장탄수
        int max = max_amount;

        max += this.buffedLvl();

        return max;
    }

    @Override
    public Boomer knockItem(){
        return new Thrower();
    }

    public class Thrower extends Boomer {
        {
            image = ItemSpriteSheet.HAND_GRENADE;
        }

        @Override
        protected void activate(int cell) {
            Char ch = Actor.findChar(cell);
            if (ch != null) {
                Buff.affect(ch, Paralysis.class, 2f);
            }

            WandOfBlastWave.BlastWave.blast(cell);
            Sample.INSTANCE.play( Assets.Sounds.BLAST );

            for (int i : PathFinder.NEIGHBOURS8) {
                int c = cell + i;
                Char target = Actor.findChar(c);
                if (target != null) {
                    Ballistica trajectory = new Ballistica(cell, target.pos, Ballistica.STOP_TARGET);
                    trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
                    WandOfBlastWave.throwChar(target,
                            trajectory,
                            2+curItem.buffedLvl(),
                            false,
                            true,
                            this);
                }
            }

        }
    }
}


