package com.shatteredpixel.shatteredpixeldungeon.items.active;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlashGrenade;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class HandGrenade extends Grenade {

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.5f );

    {
        image = ItemSpriteSheet.HAND_GRENADE;

        max_amount = 1;
        amount = max_amount;

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
    public ItemSprite.Glowing glowing() {
        if (hero.buff(FlashGrenade.class) != null) {
            return WHITE;
        } else {
            return null;
        }
    }

    @Override
    public Boomer knockItem(){
        return new Blaster();
    }

    public class Blaster extends Boomer {
        {
            image = ItemSpriteSheet.HAND_GRENADE;
        }

        @Override
        protected void activate(int cell) {
            new Bomb.MagicalBomb().explode(cell);
            ArrayList<Char> targets = new ArrayList<>();
            if (hero.buff(FlashGrenade.class) != null) {
                for (int i : PathFinder.NEIGHBOURS9) {
                    int c = cell + i;
                    Char ch = Actor.findChar(c);
                    if (ch != null) {
                        targets.add(ch);
                    }
                }
                for (Char ch : targets) {
                    Buff.affect(ch, Blindness.class, 1+hero.pointsInTalent(Talent.STUNDRONE_1));
                }
            }

        }
    }
}


