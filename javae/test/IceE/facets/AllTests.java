// **********************************************************************
//
// Copyright (c) 2003-2007 ZeroC, Inc. All rights reserved.
//
// This copy of Ice-E is licensed to you under the terms described in the
// ICEE_LICENSE file included in this distribution.
//
// **********************************************************************

import Test.*;

public class AllTests
{
    private static void
    test(boolean b)
    {
        if(!b)
        {
            throw new RuntimeException();
        }
    }

    public static GPrx
    allTests(Ice.Communicator communicator, java.io.PrintStream out)
    {
        out.print("testing facet registration exceptions... ");
	Ice.ObjectAdapter adapter = communicator.createObjectAdapter("FacetExceptionTestAdapter");
	Ice.Object obj = new EmptyI();
        adapter.add(obj, communicator.stringToIdentity("d"));
	adapter.addFacet(obj, communicator.stringToIdentity("d"), "facetABCD");
	try
	{
            adapter.addFacet(obj, communicator.stringToIdentity("d"), "facetABCD");
	    test(false);
	}
	catch(Ice.AlreadyRegisteredException ex)
	{
	}
	adapter.removeFacet(communicator.stringToIdentity("d"), "facetABCD");
	try
	{
            adapter.removeFacet(communicator.stringToIdentity("d"), "facetABCD");
	    test(false);
	}
	catch(Ice.NotRegisteredException ex)
	{
	}
        out.println("ok");

        out.print("testing removeAllFacets... ");
	Ice.Object obj1 = new EmptyI();
	Ice.Object obj2 = new EmptyI();
	adapter.addFacet(obj1, communicator.stringToIdentity("id1"), "f1");
	adapter.addFacet(obj2, communicator.stringToIdentity("id1"), "f2");
	Ice.Object obj3 = new EmptyI();
	adapter.addFacet(obj1, communicator.stringToIdentity("id2"), "f1");
	adapter.addFacet(obj2, communicator.stringToIdentity("id2"), "f2");
	adapter.addFacet(obj3, communicator.stringToIdentity("id2"), "");
	java.util.Hashtable fm = adapter.removeAllFacets(communicator.stringToIdentity("id1"));
	test(fm.size() == 2);
	test(fm.get("f1") == obj1);
	test(fm.get("f2") == obj2);
	try
	{
            adapter.removeAllFacets(communicator.stringToIdentity("id1"));
	    test(false);
	}
	catch(Ice.NotRegisteredException ex)
	{
	}
	fm = adapter.removeAllFacets(communicator.stringToIdentity("id2"));
	test(fm.size() == 3);
	test(fm.get("f1") == obj1);
	test(fm.get("f2") == obj2);
	test(fm.get("") == obj3);
        out.println("ok");

        adapter.deactivate();

        out.print("testing stringToProxy... ");
        out.flush();
        String ref = communicator.getProperties().getPropertyWithDefault("Test.Proxy", "d:default -p 12010 -t 10000");
        Ice.ObjectPrx db = communicator.stringToProxy(ref);
        test(db != null);
        out.println("ok");

        out.print("testing checked cast... ");
        out.flush();
        DPrx d = DPrxHelper.checkedCast(db);
        test(d != null);
        test(d.equals(db));
        out.println("ok");

        out.print("testing non-facets A, B, C, and D... ");
        out.flush();
        test(d.callA().equals("A"));
        test(d.callB().equals("B"));
        test(d.callC().equals("C"));
        test(d.callD().equals("D"));
        out.println("ok");

        out.print("testing facets A, B, C, and D... ");
        out.flush();
        DPrx df = DPrxHelper.checkedCast(d, "facetABCD");
        test(df != null);
        test(df.callA().equals("A"));
        test(df.callB().equals("B"));
        test(df.callC().equals("C"));
        test(df.callD().equals("D"));
        out.println("ok");

        out.print("testing facets E and F... ");
        out.flush();
        FPrx ff = FPrxHelper.checkedCast(d, "facetEF");
        test(ff != null);
        test(ff.callE().equals("E"));
        test(ff.callF().equals("F"));
        out.println("ok");

        out.print("testing facet G... ");
        out.flush();
        GPrx gf = GPrxHelper.checkedCast(ff, "facetGH");
        test(gf != null);
        test(gf.callG().equals("G"));
        out.println("ok");

        out.print("testing whether casting preserves the facet... ");
        out.flush();
        HPrx hf = HPrxHelper.checkedCast(gf);
        test(hf != null);
        test(hf.callG().equals("G"));
        test(hf.callH().equals("H"));
        out.println("ok");

        return gf;
    }
}
