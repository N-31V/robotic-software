/* Auto-generated by genmsg_cpp for file /home/n-31v/robotic-software/ros_workspace/nxt_lejos/nxt_lejos_msgs/msg/Tone.msg */
#ifndef NXT_LEJOS_MSGS_MESSAGE_TONE_H
#define NXT_LEJOS_MSGS_MESSAGE_TONE_H
#include <string>
#include <vector>
#include <map>
#include <ostream>
#include "ros/serialization.h"
#include "ros/builtin_message_traits.h"
#include "ros/message_operations.h"
#include "ros/time.h"

#include "ros/macros.h"

#include "ros/assert.h"


namespace nxt_lejos_msgs
{
template <class ContainerAllocator>
struct Tone_ {
  typedef Tone_<ContainerAllocator> Type;

  Tone_()
  : pitch(0)
  , duration(0)
  {
  }

  Tone_(const ContainerAllocator& _alloc)
  : pitch(0)
  , duration(0)
  {
  }

  typedef int16_t _pitch_type;
  int16_t pitch;

  typedef int16_t _duration_type;
  int16_t duration;


  typedef boost::shared_ptr< ::nxt_lejos_msgs::Tone_<ContainerAllocator> > Ptr;
  typedef boost::shared_ptr< ::nxt_lejos_msgs::Tone_<ContainerAllocator>  const> ConstPtr;
}; // struct Tone
typedef  ::nxt_lejos_msgs::Tone_<std::allocator<void> > Tone;

typedef boost::shared_ptr< ::nxt_lejos_msgs::Tone> TonePtr;
typedef boost::shared_ptr< ::nxt_lejos_msgs::Tone const> ToneConstPtr;


template<typename ContainerAllocator>
std::ostream& operator<<(std::ostream& s, const  ::nxt_lejos_msgs::Tone_<ContainerAllocator> & v)
{
  ros::message_operations::Printer< ::nxt_lejos_msgs::Tone_<ContainerAllocator> >::stream(s, "", v);
  return s;}

} // namespace nxt_lejos_msgs

namespace ros
{
namespace message_traits
{
template<class ContainerAllocator> struct IsMessage< ::nxt_lejos_msgs::Tone_<ContainerAllocator> > : public TrueType {};
template<class ContainerAllocator> struct IsMessage< ::nxt_lejos_msgs::Tone_<ContainerAllocator>  const> : public TrueType {};
template<class ContainerAllocator>
struct MD5Sum< ::nxt_lejos_msgs::Tone_<ContainerAllocator> > {
  static const char* value() 
  {
    return "e1d9b86aeb1932bcd48bbf7f748a6c8d";
  }

  static const char* value(const  ::nxt_lejos_msgs::Tone_<ContainerAllocator> &) { return value(); } 
  static const uint64_t static_value1 = 0xe1d9b86aeb1932bcULL;
  static const uint64_t static_value2 = 0xd48bbf7f748a6c8dULL;
};

template<class ContainerAllocator>
struct DataType< ::nxt_lejos_msgs::Tone_<ContainerAllocator> > {
  static const char* value() 
  {
    return "nxt_lejos_msgs/Tone";
  }

  static const char* value(const  ::nxt_lejos_msgs::Tone_<ContainerAllocator> &) { return value(); } 
};

template<class ContainerAllocator>
struct Definition< ::nxt_lejos_msgs::Tone_<ContainerAllocator> > {
  static const char* value() 
  {
    return "int16 pitch\n\
int16 duration\n\
\n\
";
  }

  static const char* value(const  ::nxt_lejos_msgs::Tone_<ContainerAllocator> &) { return value(); } 
};

template<class ContainerAllocator> struct IsFixedSize< ::nxt_lejos_msgs::Tone_<ContainerAllocator> > : public TrueType {};
} // namespace message_traits
} // namespace ros

namespace ros
{
namespace serialization
{

template<class ContainerAllocator> struct Serializer< ::nxt_lejos_msgs::Tone_<ContainerAllocator> >
{
  template<typename Stream, typename T> inline static void allInOne(Stream& stream, T m)
  {
    stream.next(m.pitch);
    stream.next(m.duration);
  }

  ROS_DECLARE_ALLINONE_SERIALIZER
}; // struct Tone_
} // namespace serialization
} // namespace ros

namespace ros
{
namespace message_operations
{

template<class ContainerAllocator>
struct Printer< ::nxt_lejos_msgs::Tone_<ContainerAllocator> >
{
  template<typename Stream> static void stream(Stream& s, const std::string& indent, const  ::nxt_lejos_msgs::Tone_<ContainerAllocator> & v) 
  {
    s << indent << "pitch: ";
    Printer<int16_t>::stream(s, indent + "  ", v.pitch);
    s << indent << "duration: ";
    Printer<int16_t>::stream(s, indent + "  ", v.duration);
  }
};


} // namespace message_operations
} // namespace ros

#endif // NXT_LEJOS_MSGS_MESSAGE_TONE_H

