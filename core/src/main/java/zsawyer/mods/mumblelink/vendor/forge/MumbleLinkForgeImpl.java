/*
 mod_MumbleLink - Positional Audio Communication for Minecraft with Mumble
 Copyright 2011-2013 zsawyer (http://sourceforge.net/users/zsawyer)

 This file is part of mod_MumbleLink
 (http://sourceforge.net/projects/modmumblelink/).

 mod_MumbleLink is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 mod_MumbleLink is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with mod_MumbleLink.  If not, see <http://www.gnu.org/licenses/>.

 */

package zsawyer.mods.mumblelink.vendor.forge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import zsawyer.mods.mumblelink.MumbleLinkAPIImpl;
import zsawyer.mods.mumblelink.MumbleLinkBase;
import zsawyer.mods.mumblelink.api.MumbleLink;
import zsawyer.mods.mumblelink.api.MumbleLinkAPI;
import zsawyer.mods.mumblelink.mumble.ExtendedUpdateData;
import zsawyer.mods.mumblelink.util.ConfigHelper;

/**
 * mod to link with mumble for positional audio
 * <p/>
 * this is a forge based implementation
 *
 * @author zsawyer, 2013-04-09
 */
// TODO: use "canBeDeactivated = true" to allow mod deactivation via FML
@Mod(modid = MumbleLink.MOD_ID, useMetadata = true)
@SideOnly(Side.CLIENT)
public class MumbleLinkForgeImpl extends MumbleLinkBase {
    public static Logger LOG;

    // The instance of the mod that Forge uses.
    @Instance(MumbleLink.MOD_ID)
    public static MumbleLinkForgeImpl instance;
    //
    private UpdateTicker updateTicker;

    private boolean enabled = true;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG = event.getModLog();

        name = event.getModMetadata().name;
        version = event.getModMetadata().version;

        if (FMLCommonHandler.instance().getSide().isServer())
            throw new RuntimeException(getName()
                    + " should not be installed on a server!");

        loadConfig(event);
    }

    private void loadConfig(FMLPreInitializationEvent event) {
        ConfigHelper configHelper = new ConfigHelper(event);

        debug = configHelper.loadDebug(debugging());
        enabled = configHelper.loadEnabled(enabled);
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void load(FMLInitializationEvent event) {
        super.load();
        initComponents();
        activate();
    }

    private void initComponents() {
        this.updateTicker = new UpdateTicker();
    }

    @Override
    public void activate() {
        updateTicker.activate();
    }

    @Override
    public void deactivate() {
        updateTicker.deactivate();
    }

}
