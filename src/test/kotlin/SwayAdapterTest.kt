import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

class SwayAdapterTest {

    @Test
    fun sendCommand() {
        assertDoesNotThrow {
            sway.sendCommand("floating toggle")
            sway.sendCommand("floating toggle")
        }
    }

    @Test
    fun getWorkspaces() {
        assertTrue(sway.workspaces.isNotEmpty())

    }

    @Test
    fun getFocusedWorkspace() {
        assertTrue(arrayOf("1", "2", "3", "4", "5").contains(sway.focusedWorkspace.name))
    }

    @Test
    fun changeWorkspace() {
        // arrange
        assumeTrue(sway.workspaces.size > 1, "Not enough workspaces to test")
        val workspace = sway.workspaces.first { !it.focused }.name
        val currentWorkspace = sway.focusedWorkspace.name

        // act
        sway.sendCommand("workspace $workspace")
        println("workspace $workspace")
        // assert
        assertEquals(sway.focusedWorkspace.name, workspace)
        sleep(1000)

        println("workspace $currentWorkspace")
        sway.sendCommand("workspace $currentWorkspace")
    }

    @Test
    fun getActiveWorkspaces() {
        assertEquals(sway.activeWorkspaces.size, 1)
    }

    @Test
    fun getOutputs() {
        assertTrue(sway.outputs.isNotEmpty())
    }

    @Test
    fun getMarks() {
        assertTrue(sway.marks.isEmpty())
    }

//    @Test
//    fun getBarConfig() {
//        val barId = sway.barIds
//        assertTrue(sway.barConfig(barId).mode.isNotEmpty())
//    }

    @Test
    fun subscribe() {
        assertTrue(sway.subscribe(arrayOf("workspace")))
    }

    @Test
    fun getBarIds() {
    }

    @Test
    fun getVersion() {
    }

    @Test
    fun getTree() {
    }
}