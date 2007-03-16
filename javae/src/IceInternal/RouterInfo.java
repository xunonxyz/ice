// **********************************************************************
//
// Copyright (c) 2003-2007 ZeroC, Inc. All rights reserved.
//
// This copy of Ice-E is licensed to you under the terms described in the
// ICEE_LICENSE file included in this distribution.
//
// **********************************************************************

package IceInternal;

public final class RouterInfo
{
    RouterInfo(Ice.RouterPrx router)
    {
        _router = router;
        _identities = new java.util.Hashtable();

	if(IceUtil.Debug.ASSERT)
	{
	    IceUtil.Debug.Assert(_router != null);
	}
    }

    synchronized public void
    destroy()
    {
	_clientProxy = null;
	_serverProxy = null;
	_adapter = null;
	_identities.clear();
    }

    public boolean
    equals(java.lang.Object obj)
    {
	if(this == obj)
	{
	    return true;
	}

	if(obj instanceof RouterInfo)
	{
	    return _router.equals(((RouterInfo)obj)._router);
	}

	return false;
    }

    public Ice.RouterPrx
    getRouter()
    {
        //
        // No mutex lock necessary, _router is immutable.
        //
        return _router;
    }

    public synchronized Ice.ObjectPrx
    getClientProxy()
    {
        if(_clientProxy == null) // Lazy initialization.
        {
            _clientProxy = _router.getClientProxy();
            if(_clientProxy == null)
            {
                throw new Ice.NoEndpointException();
            }

	    _clientProxy = _clientProxy.ice_router(null); // The client proxy cannot be routed.
	    
	    //
	    // In order to avoid creating a new connection to the
	    // router, we must use the same timeout as the already
	    // existing connection.
	    //
	    _clientProxy = _clientProxy.ice_timeout(_router.ice_getConnection().timeout());
        }

        return _clientProxy;
    }

    public synchronized void
    setClientProxy(Ice.ObjectPrx clientProxy)
    {
        _clientProxy = clientProxy.ice_router(null); // The client proxy cannot be routed.

	//
	// In order to avoid creating a new connection to the router,
	// we must use the same timeout as the already existing
	// connection.
	//
	_clientProxy = _clientProxy.ice_timeout(_router.ice_getConnection().timeout());
    }

    public Ice.ObjectPrx
    getServerProxy()
    {
        if(_serverProxy == null) // Lazy initialization.
        {
            _serverProxy = _router.getServerProxy();
            if(_serverProxy == null)
            {
                throw new Ice.NoEndpointException();
            }

            _serverProxy = _serverProxy.ice_router(null); // The server proxy cannot be routed.
        }

        return _serverProxy;
    }

    public void
    setServerProxy(Ice.ObjectPrx serverProxy)
    {
        _serverProxy = serverProxy.ice_router(null); // The server proxy cannot be routed.
    }

    public void
    addProxy(Ice.ObjectPrx proxy)
    {
        IceUtil.Debug.Assert(proxy != null);

        if(!_identities.containsKey(proxy.ice_getIdentity()))
        {
            //
            // Only add the proxy to the router if it's not already in our local map.
            //
            Ice.ObjectPrx[] proxies = new Ice.ObjectPrx[1];
            proxies[0] = proxy;
            Ice.ObjectPrx[] evictedProxies = _router.addProxies(proxies);

            //
            // If we successfully added the proxy to the router, we add it to our local map.
            //
            _identities.put(proxy.ice_getIdentity(), new java.lang.Integer(0));

            //
            // We also must remove whatever proxies the router evicted.
            //
            for(int i = 0; i < evictedProxies.length; ++i)
            {
                _identities.remove(evictedProxies[i].ice_getIdentity());
            }
        }

    }

    public synchronized void
    setAdapter(Ice.ObjectAdapter adapter)
    {
        _adapter = adapter;
    }

    public synchronized Ice.ObjectAdapter
    getAdapter()
    {
        return _adapter;
    }

    private /*final*/ Ice.RouterPrx _router;
    private Ice.ObjectPrx _clientProxy;
    private Ice.ObjectPrx _serverProxy;
    private Ice.ObjectAdapter _adapter;
    private java.util.Hashtable _identities;
}
