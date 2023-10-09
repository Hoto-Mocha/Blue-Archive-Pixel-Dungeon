package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class ShockBulletCooldown extends Buff {
    private int coolDown;
    private int maxCoolDown;

    @Override
    public int icon() {
        return BuffIndicator.TIME;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0x972F99);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (maxCoolDown - coolDown)/ maxCoolDown);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(coolDown);
    }

    public void kill() {
        coolDown --;
        if (coolDown <= 0) {
            detach();
        }
        BuffIndicator.refreshHero();
    }

    public void set() {
        maxCoolDown = 3;
        coolDown = maxCoolDown;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", coolDown, maxCoolDown);
    }

    private static final String MAXCOOLDOWN = "maxCoolDown";
    private static final String COOLDOWN  = "cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MAXCOOLDOWN, maxCoolDown);
        bundle.put(COOLDOWN, coolDown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        maxCoolDown = bundle.getInt( MAXCOOLDOWN );
        coolDown = bundle.getInt( COOLDOWN );
    }

}
