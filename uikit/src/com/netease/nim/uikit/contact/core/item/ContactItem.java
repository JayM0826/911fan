package com.netease.nim.uikit.contact.core.item;

import com.netease.nim.uikit.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.contact.core.query.TextComparator;
import com.netease.nim.uikit.contact.core.model.IContact;

import android.text.TextUtils;

/*该类扩展了通讯录数据项抽象类，要实现getItemType()和belongsGroup()方法

其实这是所有搜索结果的单条数据显示模型，所有的搜索结果都要使用他来制作成能被搜索结果面板展示的样子*/
public class ContactItem extends AbsContactItem implements Comparable<ContactItem> {
	private final IContact contact;

	private final int dataItemType;

	/**
	 *
	 * @param contact 搜索的结果，可能为用户，群成员，群，好友
	 * @param type 该条搜索结果的类型
     */
	public ContactItem(IContact contact, int type) {
		this.contact = contact;
		this.dataItemType = type;
	}

	public IContact getContact() {
		return contact;
	}

	@Override
	public int getItemType() {
		return dataItemType;
	}
	
	@Override
	public int compareTo(ContactItem item) {
		// TYPE
		int compare = compareType(item);
		if (compare != 0) {
			return compare;
		} else {
			return TextComparator.compareIgnoreCase(getCompare(), item.getCompare());
		}
	}

	@Override
	public String belongsGroup() {
		IContact contact = getContact();
		if (contact == null) {
			return ContactGroupStrategy.GROUP_NULL;
		}
		
		String group = TextComparator.getLeadingUp(getCompare());
		return !TextUtils.isEmpty(group) ? group : ContactGroupStrategy.GROUP_SHARP;
	}
	
	private String getCompare() {
		IContact contact = getContact();
		return contact != null ? contact.getDisplayName() : null;
	}
}
