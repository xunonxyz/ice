// **********************************************************************
//
// Copyright (c) 2002
// MutableRealms, Inc.
// Huntsville, AL, USA
//
// All Rights Reserved
//
// **********************************************************************

#ifndef ICE_STREAM_H
#define ICE_STREAM_H

#include <Ice/Buffer.h>

namespace _Ice
{

class ICE_API Stream : public Buffer
{
public:

    Stream();

    void writeByte(Ice::Byte value) { b.push_back(value); }
    Ice::Byte readByte();

    void writeBool(bool value) { b.push_back(static_cast<Ice::Byte>(value)); }
    bool readBool();

    void writeShort(Ice::Short);
    Ice::Short readShort();

    void writeInt(Ice::Int);
    Ice::Int readInt();

    void writeLong(Ice::Long);
    Ice::Long readLong();

    void writeFloat(Ice::Float);
    Ice::Float readFloat();

    void writeDouble(Ice::Double);
    Ice::Double readDouble();

    void writeString(const std::string&);
    void writeString(const char*); // Optimized version for const char*
    std::string readString();
};

}

#endif
