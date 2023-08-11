package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.machinegun;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
public class MachineGun extends Gun {

    {
        max_round = 4;
        reload_time = 2;
        shotPerShoot = 3;
    }

    @Override
    public int bulletMin(int lvl) {
        return (int)((2 * tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero))/3f);
    }

    @Override
    public int bulletMax(int lvl) {
        return (int)((4 * (tier+1) +
                lvl * (tier+1) +
                RingOfSharpshooting.levelDamageBonus(hero))/3f);
    }
}
