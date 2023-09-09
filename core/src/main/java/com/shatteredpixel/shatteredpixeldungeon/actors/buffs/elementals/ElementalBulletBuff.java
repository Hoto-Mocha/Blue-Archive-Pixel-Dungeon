package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
public class ElementalBulletBuff extends Buff {

    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.BULLET;
    }
}
