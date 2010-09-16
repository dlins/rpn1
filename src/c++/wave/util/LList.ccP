#ifdef __GNUC__
#pragma implementation
#endif
#include <stdlib.h>
#include <<T>LList.h>

void
<T>LList::append(const <T>LList & list)
{
	<T>LListNode::reference(list.head);
	if (head == NULL)
		head = list.head;
	else
		tail->next = list.head;
	tail = list.tail;
}

void
<T>LList::prepend(const <T>LList & list)
{
	<T>LListNode *h = list.head;
	if (h != NULL) {
		<T>LListNode *node = new <T>LListNode(h->obj);
		<T>LListNode *trailer = node;
		for (h = h->next; h != NULL; h = h->next) {
			<T>LListNode *n = new <T>LListNode(h->obj);
			trailer->next = n;
			trailer = n;
		}
		trailer->next = head;
		head = node;
	}
}

void
<T>LList::del(const <T> & obj)
{
	<T>LListNode *h = head;

	for ( ; ; ) {
		if (h == NULL) {
			tail = head = h;
			return;
		}
		else if (&h->obj == &obj) {
			<T>LListNode *n = h->next;
			if (--h->ref == 0)
				delete h;
			h = n;
		}
		else
			break;
	}

	<T>LListNode *prev = h;
	<T>LListNode *node = h->next;
	while (node != NULL) {
		if (&node->obj == &obj) {
			<T>LListNode *n = node->next;
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
