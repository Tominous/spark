/*
 * This file is part of spark.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucko.spark.forge;

import me.lucko.spark.common.sampler.TickCounter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class ForgeTickCounter implements TickCounter {
    private final TickEvent.Type type;

    private final Set<TickTask> tasks = new HashSet<>();
    private int tick = 0;

    public ForgeTickCounter(TickEvent.Type type) {
        this.type = type;
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        if (e.type != this.type) {
            return;
        }

        for (TickTask r : this.tasks){
            r.onTick(this);
        }
        this.tick++;
    }

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void close() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public int getCurrentTick() {
        return this.tick;
    }

    @Override
    public void addTickTask(TickTask runnable) {
        this.tasks.add(runnable);
    }

    @Override
    public void removeTickTask(TickTask runnable) {
        this.tasks.remove(runnable);
    }
}
