/*
 * Copyright (c) 2025 qrmcat
 *
 * This file is part of ac-drones.
 *
 * ac-drones is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * ac-drones is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ac-drones; if not, see <https://www.gnu.org/licenses/>.
 *
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.apiocraft.acdrones.client.screen;

import dan200.computercraft.client.gui.AbstractComputerScreen;
import dan200.computercraft.client.gui.GuiSprites;
import dan200.computercraft.client.gui.widgets.ComputerSidebar;
import dan200.computercraft.client.gui.widgets.TerminalWidget;
import dan200.computercraft.client.render.RenderTypes;
import dan200.computercraft.client.render.SpriteRenderer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.apiocraft.acdrones.Acdrones;
import net.apiocraft.acdrones.menu.DroneMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DroneScreen extends AbstractComputerScreen<DroneMenu> {
    private static final Identifier BACKGROUND_NORMAL = Identifier.of(Acdrones.MOD_ID, "textures/gui/dronegui.png");
    private static final Identifier BACKGROUND_ADVANCED = Identifier.of(Acdrones.MOD_ID, "textures/gui/dronegui.png");
    private static final int TEX_WIDTH = 278;
    private static final int TEX_HEIGHT = 217;
    private static final int FULL_TEX_SIZE = 512;

    public DroneScreen(DroneMenu container, PlayerInventory player, Text title) {
        super(container, player, title, 8);
        this.backgroundWidth = 295;
        this.backgroundHeight = 217;
    }

    protected TerminalWidget createTerminal() {
        return new TerminalWidget(this.terminalData, this.input, this.x + 8 + 17, this.y + 8);
    }

    protected void drawBackground(DrawContext graphics, float partialTicks, int mouseX, int mouseY) {
        boolean advanced = this.family == ComputerFamily.ADVANCED;
        Identifier texture = advanced ? BACKGROUND_ADVANCED : BACKGROUND_NORMAL;
        graphics.drawTexture(texture, this.x + 17, this.y, 0, 0.0F, 0.0F, 278, 217, 512, 512);
        int slot = this.getScreenHandler().getSelectedSlot();
        if (slot >= 0) {
            int slotX = slot % 4;
            int slotY = slot / 4;
            graphics.drawTexture(texture, this.x + 192 - 2 + slotX * 18, this.y + 134 - 2 + slotY * 18, 0, 0.0F, 217.0F, 24, 24, 512, 512);
        }

        SpriteRenderer spriteRenderer = SpriteRenderer.createForGui(graphics, RenderTypes.GUI_SPRITES);
        ComputerSidebar.renderBackground(spriteRenderer, GuiSprites.getComputerTextures(this.family), this.x, this.y + this.sidebarYOffset);
        graphics.draw();
    }
}
