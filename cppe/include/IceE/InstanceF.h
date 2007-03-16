// **********************************************************************
//
// Copyright (c) 2003-2007 ZeroC, Inc. All rights reserved.
//
// This copy of Ice-E is licensed to you under the terms described in the
// ICEE_LICENSE file included in this distribution.
//
// **********************************************************************

#ifndef ICEE_INSTANCE_F_H
#define ICEE_INSTANCE_F_H

#include <IceE/Handle.h>

namespace IceInternal
{

class Instance;
ICE_API void incRef(Instance*);
ICE_API void decRef(Instance*);
typedef IceInternal::Handle<Instance> InstancePtr;

}

#endif
