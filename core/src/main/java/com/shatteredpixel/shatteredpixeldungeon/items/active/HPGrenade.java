package com.shatteredpixel.shatteredpixeldungeon.items.active;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class HPGrenade extends Grenade {

    {
        image = ItemSpriteSheet.HP_GRENADE;

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
    public int explodeMinDmg(){
        return 2*super.explodeMinDmg();
    }

    @Override
    public int explodeMaxDmg(){
        return 2*super.explodeMaxDmg();
    }

    @Override
    public Boomer knockItem(){
        return new Blaster();
    }

    public class Blaster extends Boomer {
        {
            image = ItemSpriteSheet.HP_GRENADE;
        }

        @Override
        protected void activate(int cell) {
            explode(cell);
        }
    }
}


