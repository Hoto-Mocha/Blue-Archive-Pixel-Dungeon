package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;

public class ShotGun extends Gun {

    {
        max_round = 1;
        reload_time = 1;
        shotPerShoot = 5;
    }

    @Override
    public int bulletMin(int lvl) {
        return tier +
                lvl +
                RingOfSharpshooting.levelDamageBonus(hero);
    }

    @Override
    public int bulletMax(int lvl) {
        return 2 * (tier) +
                lvl +
                RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
    }
}
