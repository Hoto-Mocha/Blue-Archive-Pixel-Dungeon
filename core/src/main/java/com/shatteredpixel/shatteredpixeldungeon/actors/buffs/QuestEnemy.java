package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
public class QuestEnemy extends Buff {

    {
        type = buffType.POSITIVE;
    }

    protected int color = 0xFFFFFF;

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(color);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura( color );
        else target.sprite.clearAura();
    }

    {
        immunities.add(AllyBuff.class);
    }

    public static void rollForQuest(Mob m){
        if (Dungeon.mobsToQuest <= 0) Dungeon.mobsToQuest = 8;

        Dungeon.mobsToQuest--;

        if (Dungeon.mobsToQuest <= 0 && Dungeon.hero != null) {
            if (Dungeon.hero.heroClass == HeroClass.YUZU) {
                Buff.affect(m, QuestEnemy.class);
                m.state = m.WANDERING;
            }
        }
    }

    @Override
    public void detach() {
        super.detach();
        Buff.affect(Dungeon.hero, QuestEnemy.QuestEnemyTracker.class);
    }

    public static class QuestEnemyTracker extends Buff {}

}
