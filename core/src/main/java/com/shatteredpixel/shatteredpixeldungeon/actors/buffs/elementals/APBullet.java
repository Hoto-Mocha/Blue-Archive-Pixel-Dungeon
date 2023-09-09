package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBullet;
import com.watabou.noosa.Image;
public class APBullet extends ElementalBulletBuff {
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(ElementalBullet.ElementalReload.AP_BULLET.tintColor);
    }
}
