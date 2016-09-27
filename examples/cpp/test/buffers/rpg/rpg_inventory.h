// Generated with https://github.com/paidgeek/bufobjects
    

#ifndef BUFOBJECTS_RPG_INVENTORY_H
#define BUFOBJECTS_RPG_INVENTORY_H

#include "buffer_object.h"

// forward declarations

  
    
  
    namespace rpg {}namespace rpg {namespace inventory {class Inventory;}}namespace rpg {namespace inventory {class Item;}}
  

  
    
  
    namespace rpg {namespace inventory {}}
  
    
  
    
  
    namespace rpg {namespace inventory {class Item;}}
  




  
  
    
  
    namespace rpg {
  

  
    namespace inventory {
  

  


enum class Quality : uint8_t {
kCommon = 1,kRare = 2,kEpic = 3
};


  
    }
  

  
    }
  

  


  

  
  
    

  
    namespace rpg {
  

  
    namespace inventory {
  

  



class Item
: public bufobjects::BufferObject{

protected:
rpg::inventory::Quality quality_;
uint64_t cost_;
std::string name_;


public:

  typedef rpg::inventory::Item* Ptr;


Item();
Item(rpg::inventory::Quality quality,uint64_t cost,std::string name);
virtual uint32_t Size() const;
virtual void Reset();


    inline rpg::inventory::Quality GetQuality() { return quality_; }
    inline void SetQuality(rpg::inventory::Quality quality) { quality_ = quality; }
  

  
    inline const uint64_t& GetCost() const { return cost_; }
    inline void SetCost(const uint64_t& cost) { cost_ = cost; }
  

  
    inline const std::string& GetName() const { return name_; }
    inline void SetName(const std::string& name) { name_ = name; }
  

  


};


  
    }
  

  
    }
  

  


  

  
  
    

  
      namespace rpg {
  

  
      namespace inventory {
  

  


class Weapon
: public rpg::inventory::Item{

protected:
uint64_t damage_;


public:

  typedef rpg::inventory::Weapon* Ptr;




Weapon();
Weapon(uint64_t damage,rpg::inventory::Quality quality,uint64_t cost,std::string name);

  ~Weapon();

void Init(uint64_t damage,rpg::inventory::Quality quality,uint64_t cost,std::string name);
static Ptr New(uint64_t damage,rpg::inventory::Quality quality,uint64_t cost,std::string name);
Weapon(const Weapon& from);
Weapon& operator=(const Weapon& from);
explicit operator bufobjects::BufferObject::Ptr() {
  return static_cast< bufobjects::BufferObject::Ptr >(this);
}
uint16_t BufferObjectId() const;
void Reset();
void CopyTo(bufobjects::BufferObject& obj) const;
uint32_t Size() const;
void WriteTo(bufobjects::BufferObjectBuilder& bob) const;
void ReadFrom(bufobjects::BufferObjectBuilder& bob);
    inline const uint64_t& GetDamage() const { return damage_; }
    inline void SetDamage(const uint64_t& damage) { damage_ = damage; }
  

  
static void WriteDirectTo(bufobjects::BufferObjectBuilder& bob,uint64_t damage,rpg::inventory::Quality quality,uint64_t cost,std::string name);
static void WriteDirectIdentifiedTo(bufobjects::BufferObjectBuilder& bob,uint64_t damage,rpg::inventory::Quality quality,uint64_t cost,std::string name);
};




  
    }
  

  
    }
  

  


  

  
  
    

  
      namespace rpg {
  

  
      namespace inventory {
  

  


class Armor
: public rpg::inventory::Item{

protected:
uint64_t defense_;


public:

  typedef rpg::inventory::Armor* Ptr;




Armor();
Armor(uint64_t defense,rpg::inventory::Quality quality,uint64_t cost,std::string name);

  ~Armor();

void Init(uint64_t defense,rpg::inventory::Quality quality,uint64_t cost,std::string name);
static Ptr New(uint64_t defense,rpg::inventory::Quality quality,uint64_t cost,std::string name);
Armor(const Armor& from);
Armor& operator=(const Armor& from);
explicit operator bufobjects::BufferObject::Ptr() {
  return static_cast< bufobjects::BufferObject::Ptr >(this);
}
uint16_t BufferObjectId() const;
void Reset();
void CopyTo(bufobjects::BufferObject& obj) const;
uint32_t Size() const;
void WriteTo(bufobjects::BufferObjectBuilder& bob) const;
void ReadFrom(bufobjects::BufferObjectBuilder& bob);
    inline const uint64_t& GetDefense() const { return defense_; }
    inline void SetDefense(const uint64_t& defense) { defense_ = defense; }
  

  
static void WriteDirectTo(bufobjects::BufferObjectBuilder& bob,uint64_t defense,rpg::inventory::Quality quality,uint64_t cost,std::string name);
static void WriteDirectIdentifiedTo(bufobjects::BufferObjectBuilder& bob,uint64_t defense,rpg::inventory::Quality quality,uint64_t cost,std::string name);
};




  
    }
  

  
    }
  

  


  

  
  
    

  
      namespace rpg {
  

  
      namespace inventory {
  

  


class Inventory
: public bufobjects::BufferObject{

protected:
uint32_t capacity_;
std::vector<rpg::inventory::Item*> items_;


public:

  typedef rpg::inventory::Inventory* Ptr;



  class Builder;


Inventory();
Inventory(uint32_t capacity,std::vector<rpg::inventory::Item*> items);

  ~Inventory();

void Init(uint32_t capacity,std::vector<rpg::inventory::Item*> items);
static Ptr New(uint32_t capacity,std::vector<rpg::inventory::Item*> items);
Inventory(const Inventory& from);
Inventory& operator=(const Inventory& from);
explicit operator bufobjects::BufferObject::Ptr() {
  return static_cast< bufobjects::BufferObject::Ptr >(this);
}
uint16_t BufferObjectId() const;
void Reset();
void CopyTo(bufobjects::BufferObject& obj) const;
uint32_t Size() const;
void WriteTo(bufobjects::BufferObjectBuilder& bob) const;
void ReadFrom(bufobjects::BufferObjectBuilder& bob);
    inline const uint32_t& GetCapacity() const { return capacity_; }
    inline void SetCapacity(const uint32_t& capacity) { capacity_ = capacity; }
  

  
    inline const std::vector<rpg::inventory::Item*>& GetItems() const { return items_; }
    inline void SetItems(const std::vector<rpg::inventory::Item*>& items) { items_ = items; }
  

  
    
      inline rpg::inventory::Item* GetItems(int index) const { return items_[index]; }
      inline void SetItems(int index, rpg::inventory::Item* value) { items_[index] = value; }
    
  
static void WriteDirectTo(bufobjects::BufferObjectBuilder& bob,uint32_t capacity,std::vector<rpg::inventory::Item*> items);
static void WriteDirectIdentifiedTo(bufobjects::BufferObjectBuilder& bob,uint32_t capacity,std::vector<rpg::inventory::Item*> items);
};


  class Inventory::Builder {
private:
uint32_t capacity_;
std::vector<rpg::inventory::Item*> items_;

public:
  Builder();

    Builder& Capacity(const uint32_t& capacity);
  

  
    Builder& Items(const std::vector<rpg::inventory::Item*>& items);
  

  
    
      Builder& Items(int index, rpg::inventory::Item* value);
      Builder& AddItems(rpg::inventory::Item* value);
      Builder& AddItems(std::vector<rpg::inventory::Item*> values);
    
  
Inventory::Ptr Build();
void WriteTo(bufobjects::BufferObjectBuilder& bob);
void WriteIdentifiedTo(bufobjects::BufferObjectBuilder& bob);
};



  
    }
  

  
    }
  

  


  


#endif