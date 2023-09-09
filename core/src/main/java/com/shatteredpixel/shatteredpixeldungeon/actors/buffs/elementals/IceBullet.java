package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBullet;
import com.watabou.noosa.Image;
public class IceBullet extends ElementalBulletBuff {
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(ElementalBullet.ElementalReload.ICE_BULLET.tintColor);
    }
}
