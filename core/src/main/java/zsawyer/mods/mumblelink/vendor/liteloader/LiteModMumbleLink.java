/*
 * mod_MumbleLink - Positional Audio Communication for Minecraft with Mumble
 * Copyright 2014 zsawyer (http://sourceforge.net/users/zsawyer)
 *
 * This file is part of mod_MumbleLink
 * (http://sourceforge.net/projects/modmumblelink/).
 *
 * mod_MumbleLink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mod_MumbleLink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mod_MumbleLink.  If not, see <http://www.gnu.org/licenses/>.
 */

package zsawyer.mods.mumblelink.vendor.liteloader;

import com.mumfrey.liteloader.Tickable;
import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * Created by zsawyer on 30.08.2014.
 */
public class LiteModMumbleLink implements Tickable {

    private MumbleLinkLiteLoaderImpl modImpl;

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
        modImpl.tryUpdateMumble(minecraft);
    }

    @Override
    public String getVersion() {
        return modImpl.getVersion();
    }

    @Override
    public void init(File configPath) {
        modImpl = new MumbleLinkLiteLoaderImpl();
        modImpl.preInit();
        modImpl.init();
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }

    @Override
    public String getName() {
        return modImpl.getName();
    }
}
