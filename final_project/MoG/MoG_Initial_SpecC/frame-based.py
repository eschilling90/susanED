#! python
###############################################################################
# expl1.py: Exploration based on GUI Testing                                  #
###############################################################################
#
#

"""Simple GUI test."""

import os, os.path 
# for copy cmd
import shutil
import unittest
import scetester
from scetester import *


class SimpleTestCase(scetester.ApplicationTestCase):
    """Tests for application and main window."""

    # ----- Tests

    def runTest(self):
        """Testing GUI"""
        # import orginal SpecC File
        win = self.importFile("Design.sc")
        # save imported as SIR
        self.saveFile(win, "spec_alloc.sir")
        # create new project
        #self.newProject()
        # copy project template into initial template
        # any change will be inintial prj (and purged with new call)
        shutil.copyfile("../demorc/template.prj", "template.sce")
        # open template project that preset's to have simulation within SCE (no xterm)
        self.openProject("template.sce")
        
        # add open file to project
        self.addProject()
        # select behavior Design as top level behavior for exploration
        # everything inside will be refined
        self.setTopLevel("Testbench")
        # allocate a PE in cathegory of type and name it
        self.allocatePEList([{"category": "Custom Hardware",
                              "type":   "HW_Standard",
                              "name":   "STIM"},
                             {"category": "Custom Hardware",
                              "type":   "HW_Standard",
                              "name":   "BGE"},
                             {"category": "Custom Hardware",
                              "type":   "HW_Standard",
                              "name":   "MON"}])

        # select an instance in the design starting with Main behavior as root
        self.selectItem(["Main", "tb", "stim"])
        # the leaf of instance above, map to PE
        self.mapPE("STIM", "stim")
        self.mapPE("BGE",  "bge")
        self.mapPE("MON",  "moni")
        # show all channels in the system
        #self.showChannels(1)
        # map channel with instance name c1 to PE HW
        #self.mapPE("PE2", "c1")

        self.compileDesign()
        self.simulateDesign()
        self.profileDesign()
        self.analyzeDesign()
        self.evaluateDesign()
        
        # perform architecture refinement 
        win2 = self.refineArchitecture(1, 1, 1)
        # abort refinement has failed
        self.failUnless(win2)
        # of old file exists delete it
        if os.path.exists("arch.sir"):
            os.remove("arch.sir")
        # save the refined model as arch.sir
        self.renameDesign("arch.sir")
        self.saveFile(win2)

        # Save overall project as project.sce for 
        # later inspection
        self.saveProject("project.sce")

    # put unused refinement rules sepearate funciton so that they can be
    # copied later
    def unusedRefinement(self):
        # scheduling parameters
        self.scheduleBehaviors("CPU",[], SCHED_PRIORITY_BASED,
		[("bMain", 1), ("bSubLow", 4), ("bSubLowLow", 5),("bSubHigh", 3), ("bMain2", 2)] )

	# still need the scheduling refinement call
	win3 = self.refineScheduling()

        self.failUnless(win3)
	if os.path.exists("sched.sir"):
            os.remove("sched.sir")
        self.renameDesign("sched.sir")
        self.saveFile(win3)

        # default bus naming has changed from Bus0 to port name in proc
        # rename back to Bus0
	self.renameBus("PortA0", "Bus0")

        # construct base address and slave name on AHB for more convenience
        slaveName = "slave"
        slaveNr   = "4"
        if os.getenv('SLAVE_NUMBER'):
            slaveNr = os.environ['SLAVE_NUMBER']
        slaveName = slaveName + slaveNr


        # connect PE "HW"'s port "Port0" to the bus "Bus0" as "Slave"
        self.setConnectivity([("PE2", "Port0", "Bus0", [slaveName])])

        # perform network refinement  with above connectivity
        win4 = self.refineNetwork(1, 1, 1)
        self.failUnless(win4)
        if os.path.exists("net.sir"):
            os.remove("net.sir")
        self.renameDesign("net.sir")
        self.saveFile(win4)

        # assign link parameters to prepeare for communication refinement
        self.assignLinkParameters([
            {"bus":"Bus0", "link":"c_link_PE1__PE2",
             "address":"0x"+slaveNr+"0000000", "interrupt":None},
            {"bus":"Bus0", "link":"c_link_PE1__PE2_1",
             "address":"0x"+slaveNr+"0000040", "interrupt":None},
            ], 0, 0)
        self.saveFile(win4)

        # communication refinement into TLM 
        win5 = self.refineCommunication(1)
        self.failUnless(win5)
        if os.path.exists("comm_tlm.sir"):
            os.remove("comm_tlm.sir")
        self.renameDesign("comm_tlm.sir")
        self.saveFile(win5)
                
        # communication refinement into BFM
        self.activateFile(win4)
        win6 = self.refineCommunication(0)
        self.failUnless(win6)
        if os.path.exists("comm_bf.sir"):
            os.remove("comm_bf.sir")
        self.renameDesign("comm_bf.sir")
        self.saveFile(win6)

        # ISS integration
        # Use win6 (BFM) as basis for refinement
        self.activateFile(win6)
        win8 = self.generateCCode(TRUE);
        self.failUnless(win8)
        if os.path.exists("comm_bf_iss.sir"):
            os.remove("comm_bf_iss.sir")
        self.renameDesign("comm_bf_iss.sir")
        self.saveFile(win8)

        # Generate C code and intergrate 
        # Use win6 (BFM) as basis for refinement
        self.activateFile(win6)
        win7 = self.generateCCode();
        self.failUnless(win7)
        if os.path.exists("comm_bf_c.sir"):
            os.remove("comm_bf_c.sir")
        self.renameDesign("comm_bf_c.sir")
        self.saveFile(win7)

        self.saveFile()

        # Save overall project as project.sce for 
        # later inspection
        self.saveProject("project.sce")


def suite():
    """Create test suite for unit testing of module."""
    testSuite = unittest.TestSuite()
    simpleTest = SimpleTestCase()
    testSuite.addTest(simpleTest)
    return testSuite


if __name__ == "__main__":
    unittest.main(defaultTest = 'suite')


