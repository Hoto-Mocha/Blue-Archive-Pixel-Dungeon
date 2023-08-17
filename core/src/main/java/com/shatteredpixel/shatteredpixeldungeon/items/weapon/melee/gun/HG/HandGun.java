package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
public class HandGun extends Gun {

    {
        max_round = 4;
        reload_time = 2;
        shotPerShoot = 1;
    }

    @Override
    public int bulletMin(int lvl) {
        return 2 * tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int bulletMax(int lvl) {
        return 4 * (tier+1) +
                lvl * (tier+1) +
                RingOfSharpshooting.levelDamageBonus(hero);
    }
}
