package com.epam.hubarevich.domain;

/**
 * Class used to represent Role entity
 * @author Anton_Hubarevich
 * @version 1.0
 */
public class Role extends Domain {
    private static final long serialVersionUID = 1L;

    /**
     * Unique role identifier
     */
    private Long roleId;
    /**
     * Role name. Represents user role
     */
    private String roleName;

    public Role() {
    }

    /**
     * Constructor
     * @param roleId positive Long value
     * @param roleName String value. Limit 50 symbols
     */
    public Role(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role role = (Role) o;

        if (!roleId.equals(role.roleId)) {
            return false;
        }
        return roleName.equals(role.roleName);

    }

    @Override
    public int hashCode() {
        int result = roleId.hashCode();
        result = 31 * result + roleName.hashCode();
        return result;
    }
}
