////////////////////////// University.kt ///////////////////////////////////
///////////////////////// Author: Edward Kirr /////////////////////////////
//// Originally made by Charles Clark & William Bladon-Whittam for AE1 ///
//Description: Used to stored building objects and interact with them, //
// this acts as the facade class for the building class and room class /
//////////// In this program, it's also a singleton, //////////////////
////////// as only one instance of the class is made /////////////////
/////////////////// through the whole application ///////////////////
package logic


class University(val name: String) {
    /**
     * University object to store all the buildings of the University
     * There should only be one university required for the project - but multiple can be used
     */
    private var buildings = mutableListOf<Building>()

    fun getBuildings() : List<Building> {
        return buildings
    }

    private fun addBuilding(building: Building) {
        buildings.add(building)
    }

    fun createBuilding(name: String, code: String): Building {
        val building = Building(name, code, this)
        this.addBuilding(building)
        return building
    }

    // Assume all buildings have different names
    fun findBuildingByName (searchName: String) : Building? {
        for(currentBuilding in buildings) {
            if(currentBuilding.name == searchName) {
                return currentBuilding
            }
        }
        return null
    }


    override fun toString() : String {
        return "University: $name Buildings: $buildings"
    }
}