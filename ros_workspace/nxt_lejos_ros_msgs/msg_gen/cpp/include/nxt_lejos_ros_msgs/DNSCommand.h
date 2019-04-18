/* Auto-generated by genmsg_cpp for file /home/n-31v/robotic-software/ros_workspace/nxt_lejos_ros_msgs/msg/DNSCommand.msg */
#ifndef NXT_LEJOS_ROS_MSGS_MESSAGE_DNSCOMMAND_H
#define NXT_LEJOS_ROS_MSGS_MESSAGE_DNSCOMMAND_H
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


namespace nxt_lejos_ros_msgs
{
template <class ContainerAllocator>
struct DNSCommand_ {
  typedef DNSCommand_<ContainerAllocator> Type;

  DNSCommand_()
  : type()
  , value(0.0)
  {
  }

  DNSCommand_(const ContainerAllocator& _alloc)
  : type(_alloc)
  , value(0.0)
  {
  }

  typedef std::basic_string<char, std::char_traits<char>, typename ContainerAllocator::template rebind<char>::other >  _type_type;
  std::basic_string<char, std::char_traits<char>, typename ContainerAllocator::template rebind<char>::other >  type;

  typedef double _value_type;
  double value;


  typedef boost::shared_ptr< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> > Ptr;
  typedef boost::shared_ptr< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator>  const> ConstPtr;
}; // struct DNSCommand
typedef  ::nxt_lejos_ros_msgs::DNSCommand_<std::allocator<void> > DNSCommand;

typedef boost::shared_ptr< ::nxt_lejos_ros_msgs::DNSCommand> DNSCommandPtr;
typedef boost::shared_ptr< ::nxt_lejos_ros_msgs::DNSCommand const> DNSCommandConstPtr;


template<typename ContainerAllocator>
std::ostream& operator<<(std::ostream& s, const  ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> & v)
{
  ros::message_operations::Printer< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> >::stream(s, "", v);
  return s;}

} // namespace nxt_lejos_ros_msgs

namespace ros
{
namespace message_traits
{
template<class ContainerAllocator> struct IsMessage< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> > : public TrueType {};
template<class ContainerAllocator> struct IsMessage< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator>  const> : public TrueType {};
template<class ContainerAllocator>
struct MD5Sum< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> > {
  static const char* value() 
  {
    return "0f40ae65f9de18f2931c26b879f34c47";
  }

  static const char* value(const  ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> &) { return value(); } 
  static const uint64_t static_value1 = 0x0f40ae65f9de18f2ULL;
  static const uint64_t static_value2 = 0x931c26b879f34c47ULL;
};

template<class ContainerAllocator>
struct DataType< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> > {
  static const char* value() 
  {
    return "nxt_lejos_ros_msgs/DNSCommand";
  }

  static const char* value(const  ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> &) { return value(); } 
};

template<class ContainerAllocator>
struct Definition< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> > {
  static const char* value() 
  {
    return "string type\n\
float64 value\n\
\n\
\n\
";
  }

  static const char* value(const  ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> &) { return value(); } 
};

} // namespace message_traits
} // namespace ros

namespace ros
{
namespace serialization
{

template<class ContainerAllocator> struct Serializer< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> >
{
  template<typename Stream, typename T> inline static void allInOne(Stream& stream, T m)
  {
    stream.next(m.type);
    stream.next(m.value);
  }

  ROS_DECLARE_ALLINONE_SERIALIZER
}; // struct DNSCommand_
} // namespace serialization
} // namespace ros

namespace ros
{
namespace message_operations
{

template<class ContainerAllocator>
struct Printer< ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> >
{
  template<typename Stream> static void stream(Stream& s, const std::string& indent, const  ::nxt_lejos_ros_msgs::DNSCommand_<ContainerAllocator> & v) 
  {
    s << indent << "type: ";
    Printer<std::basic_string<char, std::char_traits<char>, typename ContainerAllocator::template rebind<char>::other > >::stream(s, indent + "  ", v.type);
    s << indent << "value: ";
    Printer<double>::stream(s, indent + "  ", v.value);
  }
};


} // namespace message_operations
} // namespace ros

#endif // NXT_LEJOS_ROS_MSGS_MESSAGE_DNSCOMMAND_H

