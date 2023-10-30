package com.shatteredpixel.shatteredpixeldungeon.items.active;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class FlashGrenade extends Grenade {

    {
        image = ItemSpriteSheet.FLASH_GRENADE;

        max_amount = 1;
        amount = max_amount;
        dropChance = 0.10f;

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
    public Boomer knockItem(){
        return new Thrower();
    }

    public class Thrower extends Boomer {
        {
            image = ItemSpriteSheet.FLASH_GRENADE;
        }

        @Override
        protected void activate(int cell) {
            Sample.INSTANCE.play( Assets.Sounds.BLAST ); //'꿍' 하는 소리 출력
            GameScene.flash(0x80FFFFFF);           //게임 화면이 번쩍! 함
            Splash.at( cell, 0xCCFFFFFF, 1 );   //해당 색깔의 파티클 하나를 발생시킴
            for (int i : PathFinder.NEIGHBOURS25) {     //5x5지역을 대상으로 함
                int c = cell + i;
                Char target = Actor.findChar(c);
                if (target != null) {
                    Buff.affect(target, Blindness.class, 5+2*FlashGrenade.this.buffedLvl());
                }
            }

        }
    }
}


