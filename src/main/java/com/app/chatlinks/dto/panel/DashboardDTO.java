package com.app.chatlinks.dto.panel;

public class DashboardDTO {
    private Long users = 0L;
    private Long admins = 0L;
    private Long mods = 0L;
    private Long rj = 0L;
    private Long banned = 0L;
    private Long customs = 0L;
    private Long mutes = 0L;

    public Long getUsers() {
        return users;
    }

    public void setUsers(Long users) {
        this.users = users;
    }

    public Long getAdmins() {
        return admins;
    }

    public void setAdmins(Long admins) {
        this.admins = admins;
    }

    public Long getMods() {
        return mods;
    }

    public void setMods(Long mods) {
        this.mods = mods;
    }

    public Long getRj() {
        return rj;
    }

    public void setRj(Long rj) {
        this.rj = rj;
    }

    public Long getBanned() {
        return banned;
    }

    public void setBanned(Long banned) {
        this.banned = banned;
    }

    public Long getCustoms() {
        return customs;
    }

    public void setCustoms(Long customs) {
        this.customs = customs;
    }

    public Long getMutes() {
        return mutes;
    }

    public void setMutes(Long mutes) {
        this.mutes = mutes;
    }
}
