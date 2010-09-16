#ifdef __GNUC__
#pragma implementation
#endif
#include <stdlib.h>
#include "ADTADV.h"

void
ADVListNode::dereference(ADVListNode * node)
{
	while (node != NULL && --node->ref == 0) {
		ADVListNode *n = node->next;
		delete node;
		node = n;
	}
}

void
ADVList::append(const ADVList & list)
{
	ADVListNode::reference(list.head);
	if (head == NULL)
		head = list.head;
	else
		tail->next = list.head;
	tail = list.tail;
}

void
ADVList::prepend(const ADVList & list)
{
	ADVListNode *h = list.head;
	if (h != NULL) {
		ADVListNode *node = new ADVListNode(h->adv);
		ADVListNode *trailer = node;
		for (h = h->next; h != NULL; h = h->next) {
			ADVListNode *n = new ADVListNode(h->adv);
			trailer->next = n;
			trailer = n;
		}
		trailer->next = head;
		head = node;
	}
}

void
ADVList::del(const ADV & adv)
{
	ADVListNode *h = head;

	for ( ; ; ) {
		if (h == NULL) {
			tail = head = h;
			return;
		}
		else if (&h->adv == &adv) {
			ADVListNode *n = h->next;
			if (--h->ref == 0)
				delete h;
			h = n;
		}
		else
			break;
	}

	ADVListNode *prev = h;
	ADVListNode *node = h->next;
	while (node != NULL) {
		if (&node->adv == &adv) {
			ADVListNode *n = node->next;
			if (--node->ref == 0)
				delete node;
			prev->next = n;
			node = n;
		}
		else {
			prev = node;
			node = node->next;
		}
	}
	head = h;
	tail = prev;
}

void
ADT::synchronize(void) const
{
	for (Pix p = list.first(); p != NULL; list.next(p))
		list(p).update();
}
