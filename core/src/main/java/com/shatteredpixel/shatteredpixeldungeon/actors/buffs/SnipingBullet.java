package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
public class SnipingBullet extends Buff {

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() { return BuffIndicator.SNIPING; }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

}
