#ifndef _ADTADV_h
#define _ADTADV_h
#ifdef __GNUC__
#pragma interface
#endif

#include <iostream>
#include <stddef.h>
#include "Pix.h"

class ADT;

class ADV
{
public:
	ADV(const ADT &owner__);
	ADV(const ADV &adv);
	virtual ~ADV(void);

	const ADT &owner(void) const;

	virtual void update(void) = 0;

private:
	const ADT &owner_;

	ADV &operator=(const ADV &adv);		// do not define
};

// NOTE:  This list class is adapted from the one described by
// Lippman in his second edition of the C++Primer.  The handling
// of references is adapted from List.{h,cc}P in the GNU C++ library.

class ADVListNode
{
private:
	ADVListNode(ADV & adv_);
	ADVListNode(ADV & adv_, ADVListNode * next_);

	static void reference(ADVListNode * node);
	static void dereference(ADVListNode * node);

	ADV &adv;
	ADVListNode *next;
	short ref;

	friend class ADVList;
};

class ADVList
{
public:
	ADVList(void);
	ADVList(ADV & adv);
	ADVList(const ADVList & list);
	~ADVList(void);
	ADVList & operator = (const ADVList & list);

	int null(void) const;

	void empty(void);
	void append(ADV & adv);
	void append(const ADVList & list);
	void prepend(ADV & adv);
	void prepend(const ADVList & list);
	void del(const ADV & adv);

	Pix first(void) const;
	void next(Pix & p) const;
	const ADV & operator() (Pix p) const;
	ADV & operator() (Pix p);

private:
	ADVListNode *head;
	ADVListNode *tail;
};

class ADT
{
public:
	ADT(void);
	ADT(const ADT &adt);
	virtual ~ADT(void);
	ADT &operator=(const ADT &adt);

	// Note: Constancy here is only logical, not literal.
	// The list part of the ADT changes, but the rest does not.
	virtual void synchronize(void) const;
	inline void attach(ADV &adv) const;
	inline void detach(const ADV &adv) const;

private:
	mutable ADVList list;
};

inline const ADT &
ADV::owner(void) const
{
	return owner_;
}

inline
ADV::ADV(const ADT &owner__)
	: owner_(owner__)
{
	owner().attach(*this);
}

inline
ADV::ADV(const ADV &adv)
	: owner_(adv.owner())
{
	owner().attach(*this);
}

inline
ADV::~ADV(void)
{
	owner().detach(*this);
}

inline
ADVListNode::ADVListNode(ADV & adv_)
	: adv(adv_),
	  ref(1)
{
}

inline
ADVListNode::ADVListNode(ADV & adv_,
	ADVListNode * next_)
	: adv(adv_),
	  next(next_),
	  ref(1)
{
}

inline void
ADVListNode::reference(ADVListNode * node)
{
	if (node != NULL)
		++node->ref;
}

inline
ADVList::ADVList(void)
	: head(NULL),
	  tail(NULL)
{
}

inline
ADVList::ADVList(ADV & adv)
	: head(new ADVListNode(adv, NULL)),
	  tail(head)
{
}

inline
ADVList::ADVList(const ADVList & list)
	: head(list.head),
	  tail(list.tail)
{
	ADVListNode::reference(list.head);
}

inline
ADVList::~ADVList(void)
{
	ADVListNode::dereference(head);
}

inline ADVList &
ADVList::operator = (const ADVList & list)
{
	if (this != &list) {
		ADVListNode::dereference(head);
		ADVListNode::reference(list.head);
		head = list.head;
		tail = list.tail;
	}
	return *this;
}

inline int
ADVList::null(void) const
{
	return head == NULL;
}

inline void
ADVList::empty(void)
{
	ADVListNode::dereference(head);
	head = NULL;
	tail = NULL;
}

inline void
ADVList::append(ADV & adv)
{
	ADVListNode *node = new ADVListNode(adv, NULL);
	if (head == NULL)
		head = node;
	else
		tail->next = node;
	tail = node;
}

inline void
ADVList::prepend(ADV & adv)
{
	ADVListNode *node = head;
	head = new ADVListNode(adv, node);
	if (node == NULL)
		tail = head;
}

inline Pix
ADVList::first(void) const
{
	return Pix(head);
}

inline void
ADVList::next(Pix & p) const
{
	if (p != NULL)
		p = Pix(((ADVListNode *) p)->next);
}

inline const ADV &
ADVList::operator() (Pix p) const
{
	return ((ADVListNode *) p)->adv;
}

inline ADV &
ADVList::operator() (Pix p)
{
	return ((ADVListNode *) p)->adv;
}

inline
ADT::ADT(void)
	: list()
{
}

inline
ADT::ADT(const ADT &adt)
	: list(adt.list)
{
}

inline
ADT::~ADT(void)
{
}

inline ADT &
ADT::operator=(const ADT &adt)
{
	list = adt.list;
	return *this;
}

inline void
ADT::attach(ADV &adv) const
{
	list.append(adv);
}

inline void
ADT::detach(const ADV &adv) const
{
	list.del(adv);
}

#endif /* _ADTADV_h */
