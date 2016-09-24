// Generated with https://github.com/paidgeek/bufobjects

#include "weapon.h"



  
    namespace rpg {
  

  
    namespace inventory {
  

  


Weapon::Weapon() { }

Weapon::Weapon(uint64_t damage,std::string name,rpg::inventory::Quality quality,uint64_t cost)
:rpg::inventory::Item(name,quality,cost)
  
    ,
  damage_(damage){}

void Weapon::Init(uint64_t damage,std::string name,rpg::inventory::Quality quality,uint64_t cost) {damage_ = damage;name_ = name;quality_ = quality;cost_ = cost;}

Weapon::Weapon(const Weapon& from) {
  from.CopyTo(*this);
}

Weapon& Weapon::operator=(const Weapon& from) {
  from.CopyTo(*this);
  return *this;
}

uint16_t Weapon::BufferObjectId() const {
  return bufobjects::BufferObject::kRpgInventoryWeaponId;
}

void Weapon::Reset() {
rpg::inventory::Item::Reset();damage_ = 0;

}

void Weapon::CopyTo(bufobjects::BufferObject& obj) const {
Weapon& dst = static_cast< Weapon& >(obj);

dst.damage_ = damage_;dst.name_ = name_;dst.quality_ = quality_;dst.cost_ = cost_;
}

uint32_t Weapon::Size() const {
uint32_t size = rpg::inventory::Item::Size();

size += 8; // size for "u64"
  
return size;
}

void Weapon::WriteTo(bufobjects::BufferObjectBuilder& bob) const {
uint32_t needed = Size();
if(bob.GetRemaining() < needed) {
bob.GrowBuffer(needed);
}
{
    bob.WriteUInt64(damage_);
  
  }{
    bob.WriteString(name_);
  
  }{
    bob.WriteUInt8(static_cast< uint8_t >(quality_));
  
  }{
    bob.WriteUInt64(cost_);
  
  }
}

void Weapon::ReadFrom(bufobjects::BufferObjectBuilder& bob) {
{
    damage_ = bob.ReadUInt64();
  
  }{
    name_ = bob.ReadString();
  
  }{
    quality_ = static_cast< rpg::inventory::Quality >(bob.ReadUInt8());
  
  }{
    cost_ = bob.ReadUInt64();
  
  }
}const uint64_t& Weapon::GetDamage() const {
    return damage_;
  }

  void Weapon::SetDamage(const uint64_t& damage) {
    damage_ = damage;
  }

  
Weapon::Builder::Builder() { }
Weapon::Builder& Weapon::Builder::SetDamage(const uint64_t& damage) {
    damage_ = damage;
    return *this;
  }
  Weapon::Builder& Weapon::Builder::SetName(const std::string& name) {
    name_ = name;
    return *this;
  }
  Weapon::Builder& Weapon::Builder::SetQuality(const rpg::inventory::Quality& quality) {
    quality_ = quality;
    return *this;
  }
  Weapon::Builder& Weapon::Builder::SetCost(const uint64_t& cost) {
    cost_ = cost;
    return *this;
  }
  
std::shared_ptr< Weapon > Weapon::Builder::Build() {
  return std::shared_ptr< Weapon >{ new Weapon{
  damage_,name_,quality_,cost_
  } };
}


  
    }
  

  
    }