package de.wirvsvirus.neighborhoodaid.db.model;

public class DbRoot {

        private final UserTable userTable = new UserTable();
        private final ShoppingListTable shoppingListTable = new ShoppingListTable();

        public UserTable getUserTable() {
                return userTable;
        }

        public ShoppingListTable getShoppingListTable() {
                return shoppingListTable;
        }
}
