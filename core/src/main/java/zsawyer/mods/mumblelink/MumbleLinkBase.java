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

package zsawyer.mods.mumblelink;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zsawyer.mods.mumblelink.api.MumbleLink;
import zsawyer.mods.mumblelink.api.MumbleLinkAPI;
import zsawyer.mods.mumblelink.error.ErrorHandlerImpl;
import zsawyer.mods.mumblelink.error.ModErrorHandler.ModError;
import zsawyer.mods.mumblelink.loader.PackageLibraryLoader;
import zsawyer.mods.mumblelink.mumble.ExtendedUpdateData;
import zsawyer.mods.mumblelink.mumble.MumbleInitializer;
import zsawyer.mods.mumblelink.mumble.UpdateData;
import zsawyer.mumble.jna.LinkAPILibrary;

/**
 * mod to link with mumble for positional audio
 * <p/>
 * http://mumble.sourceforge.net/
 *
 * @author zsawyer, 2013-04-09
 *         <p/>
 *         <p/>
 *         when developing for it I suggest using "mumblePAHelper" to see
 *         coordinates
 */
public abstract class MumbleLinkBase implements MumbleLink {
    public static Logger LOG = LogManager.getLogger(MumbleLinkBase.class.getSimpleName());

    public static MumbleLinkBase instance;

    protected MumbleInitializer mumbleInititer;
    protected Thread mumbleInititerThread;
    protected UpdateData mumbleData;
    protected LinkAPILibrary library;
    public ErrorHandlerImpl errorHandler;
    protected MumbleLinkAPIImpl api;

    protected String name = "MumbleLink";
    protected String version = "unknown";
    protected boolean debug = false;

    public MumbleLinkBase() {
        super();
        assert instance == null;
        instance = this;
    }

    public void load() {
        errorHandler = new ErrorHandlerImpl(this);
        initComponents();
    }

    private void initComponents() {
        try {
            library = new PackageLibraryLoader()
                    .loadLibrary(MumbleLinkConstants.LIBRARY_NAME);
        } catch (Exception e) {
            errorHandler.throwError(ModError.LIBRARY_LOAD_FAILED, e);
        }

        mumbleData = new UpdateData(library, errorHandler);

        mumbleInititer = new MumbleInitializer(library, errorHandler);
        mumbleInititerThread = new Thread(mumbleInititer);

        ExtendedUpdateData extendedUpdateData = new ExtendedUpdateData(library,
                errorHandler);
        mumbleData = extendedUpdateData;

        api = new MumbleLinkAPIImpl();
        api.setExtendedUpdateData(extendedUpdateData);
    }

    public void tryUpdateMumble(Minecraft game) {
        if (mumbleInititer.isMumbleInitialized()) {
            if (game.thePlayer != null && game.theWorld != null) {
                mumbleData.set(game);
                mumbleData.send();
            }
        } else {
            try {
                mumbleInititerThread.start();
            } catch (IllegalThreadStateException ex) {
                // thread was already started so we do nothing
            }
        }
    }

    @Override
    public MumbleLinkAPI getApi() {
        return api;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean debugging() {
        return debug;
    }
}