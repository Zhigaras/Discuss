package com.zhigaras.home.presentation

import com.zhigaras.adapterdelegate.Payload

class TitleChanged(val newTitle: String) : Payload

class SupportListSizeChanged(val newListSize: Int) : Payload

class AgainstListSizeChanged(val newListSize: Int) : Payload